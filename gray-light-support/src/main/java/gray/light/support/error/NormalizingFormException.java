package gray.light.support.error;

public class NormalizingFormException extends Exception {

    public NormalizingFormException(String message) {
        super(message);
    }

    public static void emptyProperty(String key) throws NormalizingFormException {
        throw new NormalizingFormException("The property's value of " + key + " is empty.");
    }
}
