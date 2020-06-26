package gray.light.support.error;

public class ExtractRequestParamException extends Exception {

    public ExtractRequestParamException(String message) {
        super(message);
    }

    public ExtractRequestParamException(String message, Throwable cause) {
        super(message, cause);
    }
}
