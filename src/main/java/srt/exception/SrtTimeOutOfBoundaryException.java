package srt.exception;

public class SrtTimeOutOfBoundaryException extends Exception{
    public SrtTimeOutOfBoundaryException() {
    }

    public SrtTimeOutOfBoundaryException(String message) {
        super(message);
    }

    public SrtTimeOutOfBoundaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SrtTimeOutOfBoundaryException(Throwable cause) {
        super(cause);
    }

    public SrtTimeOutOfBoundaryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
