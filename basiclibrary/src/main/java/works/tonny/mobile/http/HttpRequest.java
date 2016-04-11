package works.tonny.mobile.http;

import java.io.File;
import java.io.OutputStream;

public interface HttpRequest {

	public static enum Method {
		Get, Post
	}

	HttpRequest setFormParam(String name, String value);

	HttpRequest addFormParam(String name, String value);

	HttpRequest setHeader(String name, String value);

	HttpRequest addHeader(String name, String value);

	HttpRequest addFile(String name, File file);

	HttpRequest useCookieManager();

	int getResponseCode();

	byte[] executeToBytes() throws HttpRequestException, AuthException;

	void executeToOutputStream(OutputStream targetOutputStream) throws HttpRequestException, AuthException;

	void executeToFile(File file) throws HttpRequestException, AuthException;

	String executeToString() throws HttpRequestException, AuthException;

	void cancel();

}
