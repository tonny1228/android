package works.tonny.mobile.http;

/**
 * Created by tonny on 2015/10/18.
 */
public class AuthException extends Exception {
    public AuthException() {
        super();
    }

    public AuthException(String detailMessage) {
        super(detailMessage);
    }

    public AuthException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AuthException(Throwable throwable) {
        super(throwable);
    }
}
