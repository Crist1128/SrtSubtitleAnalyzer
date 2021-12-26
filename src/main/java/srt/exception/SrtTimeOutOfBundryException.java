package srt.exception;

public class SrtTimeOutOfBundryException extends Exception{
    public SrtTimeOutOfBundryException() {
    }

    public SrtTimeOutOfBundryException(String message) {
        super(message);
    }

    public SrtTimeOutOfBundryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SrtTimeOutOfBundryException(Throwable cause) {
        super(cause);
    }

    public SrtTimeOutOfBundryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
