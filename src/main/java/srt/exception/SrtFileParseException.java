package srt.exception;

public class SrtFileParseException extends Exception{
    public SrtFileParseException() {
    }

    public SrtFileParseException(String message) {
        super(message);
    }

    public SrtFileParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SrtFileParseException(Throwable cause) {
        super(cause);
    }
}
