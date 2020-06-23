public class ValidationException extends RuntimeException {

    /**
     * Constructor pentru clasa ValidationException
     * @param message : String - pentru erori
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor default
     */
    public ValidationException() {
    }
}
