package srt.exception;

public class SrtTimeMinuteOutOfBoundaryException extends SrtTimeOutOfBundryException{
    public static final String msg = "SrtTimeMinuteOutOfBoundaryException";
    public SrtTimeMinuteOutOfBoundaryException() {
        super(msg);
    }
}
