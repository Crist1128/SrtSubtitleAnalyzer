package srt.exception;

public class SrtTimeSecondOutOfBoundaryException extends SrtTimeOutOfBundryException {
    public static final String msg = "SrtTimeSecondOutOfBoundaryException";

    public SrtTimeSecondOutOfBoundaryException() {
        super(msg);
    }
}
