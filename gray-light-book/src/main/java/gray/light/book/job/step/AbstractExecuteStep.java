package gray.light.book.job.step;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractExecuteStep<T> {

    /**
     * 当一个任务成功后，会调用此方法，特别地该方法会在不同的线程执行
     */
    protected Consumer<T> success;

    /**
     * 当一个任务失败后，会调用此方法，特别地该方法会在不同的线程执行
     */
    protected BiConsumer<T, Throwable> failed;

    protected void failed(T t, Throwable e) {
        if (failed != null) {
            failed.accept(t, e);
        }
    }

    protected void success(T t) {
        if (success != null) {
            success.accept(t);
        }
    }

    protected void complete(T t, Throwable e) {
        if (e == null) {
            success(t);
        } else {
            failed(t, e);
        }
    }

    public void setSuccess(Consumer<T> success) {
        this.success = success;
    }

    public void setFailed(BiConsumer<T, Throwable> failed) {
        this.failed = failed;
    }
}
