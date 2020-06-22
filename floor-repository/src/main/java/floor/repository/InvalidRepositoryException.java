package floor.repository;

public class InvalidRepositoryException extends RuntimeException {

    public InvalidRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
