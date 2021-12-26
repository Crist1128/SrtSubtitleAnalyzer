package srt.exception;

public class SrtTimeMsecondOutOfBoundaryException extends SrtTimeOutOfBundryException{
    public static final String msg = "SrtTimeMsecondOutOfBoundaryException";
    public SrtTimeMsecondOutOfBoundaryException() {
        super(msg);
    }
}
