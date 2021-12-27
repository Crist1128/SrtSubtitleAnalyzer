package srt.exception;

public class SrtTimeMinuteOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "SrtTimeMinuteOutOfBoundaryException";
    public SrtTimeMinuteOutOfBoundaryException() {
        super(msg);
    }
}
