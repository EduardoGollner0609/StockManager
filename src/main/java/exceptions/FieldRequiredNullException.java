package exceptions;

public class FieldRequiredNullException extends RuntimeException {

    public FieldRequiredNullException(String msg) {
        super(msg);
    }

    public FieldRequiredNullException() {
        super();
    }
}
