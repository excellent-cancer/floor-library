package excellent.cancer.gray.light.jdbc;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ReactiveRepository<T, ID> {

    @Getter
    @NonNull
    private final CrudRepository<T, ID> originalRepository;

    @NonNull
    private final Executor executor;


    public <S extends T> Mono<S> save(S entity) {
        return map(() -> originalRepository.save(entity));
    }

    public <S extends T> Mono<Iterable<S>> saveAll(Iterable<S> entities) {
        return map(() -> originalRepository.saveAll(entities));
    }

    public Mono<Optional<T>> findById(ID id) {
        return map(() -> originalRepository.findById(id));
    }

    public Mono<Boolean> existsById(ID id) {
        return map(() -> originalRepository.existsById(id));
    }

    public Mono<Iterable<T>> findAll() {
        return map(originalRepository::findAll);
    }

    public Mono<Iterable<T>> findAllById(Iterable<ID> ids) {
        return map(() -> originalRepository.findAllById(ids));
    }

    public Mono<Long> count() {
        return map(originalRepository::count);
    }

    public Mono<Void> deleteById(ID id) {
        return map(() -> originalRepository.deleteById(id));
    }

    public Mono<Void> delete(T entity) {
        return map(() -> originalRepository.delete(entity));
    }

    public Mono<Void> deleteAll(Iterable<? extends T> entities) {
        return map(() -> originalRepository.deleteAll(entities));

    }

    public Mono<Void> deleteAll() {
        return map((Runnable) originalRepository::deleteAll);
    }


    private <V> Mono<V> map(Supplier<V> supplier) {
        return Mono.fromFuture(CompletableFuture.supplyAsync(supplier, executor));
    }

    private Mono<Void> map(Runnable action) {
        return Mono.fromFuture(CompletableFuture.runAsync(action, executor));
    }

}
