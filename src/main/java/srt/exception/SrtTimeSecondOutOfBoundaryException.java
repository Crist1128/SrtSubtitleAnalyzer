package srt.exception;

public class SrtTimeSecondOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "SrtTimeSecondOutOfBoundaryException";

    public SrtTimeSecondOutOfBoundaryException() {
        super(msg);
    }
}
