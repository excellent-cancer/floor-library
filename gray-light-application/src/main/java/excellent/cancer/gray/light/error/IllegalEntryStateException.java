package excellent.cancer.gray.light.error;


/**
 * 从repository取出的entry的状态与实际需求的不一致
 *
 * @author XyParaCrim
 */
public class IllegalEntryStateException extends RuntimeException {

    public IllegalEntryStateException() {
        super();
    }

    public IllegalEntryStateException(String message) {
        super(message);
    }

    public IllegalEntryStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalEntryStateException(Throwable cause) {
        super(cause);
    }

}
