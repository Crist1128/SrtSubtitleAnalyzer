package srt.exception;

public class SrtTimeMsecondOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "SrtTimeMsecondOutOfBoundaryException";
    public SrtTimeMsecondOutOfBoundaryException() {
        super(msg);
    }
}
