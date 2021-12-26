package srt.exception;

public class NullOnOperateSrtNodesListenerException extends Exception{
    private static final String msg = "NullOnOperateSrtNodesListenerException";
    public NullOnOperateSrtNodesListenerException() {
        super(msg);
    }

    public NullOnOperateSrtNodesListenerException(Throwable cause) {
        super(msg,cause);
    }
}
