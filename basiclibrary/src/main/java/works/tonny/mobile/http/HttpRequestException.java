package works.tonny.mobile.http;

/**
 * http请求异常
 * 
 * @author tonny
 *
 */
public class HttpRequestException extends Exception {

	public HttpRequestException() {
		super();
	}

	public HttpRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public HttpRequestException(String message) {
		super(message);
	}

	public HttpRequestException(Throwable cause) {
		super(cause);
	}

}
