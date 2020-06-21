package gray.light.error;

import lombok.NonNull;

/**
 * 在repository中没有查询到entity
 *
 * @author XyParaCrim
 */
public class NoSuchEntityException extends RuntimeException {

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(@NonNull Object entry) {
        super("The entity was not found in repository: \n" + entry.toString());
    }
}
