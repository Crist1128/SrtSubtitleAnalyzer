package srt.exception;

public class NullOnLoadSrtFileListenerException extends Exception{
    private static final String msg = "NullOnLoadSrtFileListenerException";
    public NullOnLoadSrtFileListenerException() {
        super(msg);
    }

    public NullOnLoadSrtFileListenerException(Throwable cause) {
        super(msg,cause);
    }
}
