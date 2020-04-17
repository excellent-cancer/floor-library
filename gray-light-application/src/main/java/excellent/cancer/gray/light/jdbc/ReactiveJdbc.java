package excellent.cancer.gray.light.jdbc;

import excellent.cancer.gray.light.error.NoSuchEntityException;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Reactive jdbc functions
 *
 * @author XyParaCrim
 */
@CommonsLog
public final class ReactiveJdbc {

    /**
     * 检查从repository中查询出的实例是否存在，若不存在则发布一个{@link NoSuchEntityException}的异常给下游
     *
     * @param fromRepository 从repository查询出的实体
     * @param fromRequest    请求的实体
     * @param sink           下游sink
     * @return 从repository中查询出的实例是否存在
     */
    public static boolean isFoundFromRepository(Object fromRepository, Object fromRequest, SynchronousSink<?> sink) {
        if (fromRepository == null) {
            sink.error(new NoSuchEntityException(fromRequest));
            return false;
        }

        return true;
    }

    public static void signalOnNotFoundEntity(Object entity, SynchronousSink<?> sink) {
        sink.error(new NoSuchEntityException(entity));
    }

    /**
     * 从查询mono中检查从repository中查询出的实例是否存在，若不存在则发布一个{@link NoSuchEntityException}的异常给下游
     *
     * @param fromRequest 查询实体
     * @param finding     查询mono
     * @param action      如果查询实体存在时执行的动作
     * @param <T>         查询实体类型
     * @param <V>         下游转换类型
     * @return 转换后的mono
     */
    public static <T, V> Mono<V> handleIfFoundEntity(T fromRequest, Mono<T> finding, BiConsumer<T, SynchronousSink<V>> action) {
        return finding.handle((fromRepository, sink) -> {
            if (isFoundFromRepository(fromRepository, fromRequest, sink)) {
                action.accept(fromRepository, sink);
            }
        });
    }

    public static <T, V> Mono<V> handleOptionalIfFoundEntity(T fromRequest, Mono<Optional<T>> finding, BiConsumer<T, SynchronousSink<V>> action) {
        return finding.handle((entityOptional, sink) -> {
            if (entityOptional.isPresent()) {
                action.accept(entityOptional.get(), sink);
            } else {
                signalOnNotFoundEntity(fromRequest, sink);
            }
        });
    }

    private static final Executor COMMON_POOL = ForkJoinPool.commonPool();

    public static <V> Mono<V> reactive(Supplier<V> supplier) {
        return Mono.fromFuture(CompletableFuture.supplyAsync(supplier, COMMON_POOL));
    }

    public static Mono<Void> reactive(Runnable action) {
        return Mono.fromFuture(CompletableFuture.runAsync(action, COMMON_POOL));
    }

    public static <T> Mono<T> flatMapIfPresent(T source, T missing) {
        return source == null ? Mono.error(new NoSuchEntityException(missing)) : Mono.just(source);
    }

    public static <T> Function<? super Optional<T>, ? extends Mono<? extends T>> flatMapperIfPresent(T missing) {
        return found -> found.isEmpty() ? Mono.error(new NoSuchEntityException(missing)) : Mono.just(found.get());
    }

    public static <T> Mono<T> flatMapIfPresent(T source, Supplier<T> missingSupplier) {
        return source == null ? Mono.error(new NoSuchEntityException(missingSupplier.get())) : Mono.just(source);
    }
}
