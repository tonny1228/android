package works.tonny.mobile.http;

import android.util.Base64;

import works.tonny.mobile.Application;

/**
 * Created by tonny on 2015/7/6.
 */
public abstract class AbstractHttpRequest implements HttpRequest {
    public static HttpRequest getInstance(HttpRequest.Method methed, String url) {
        OKHttpRequest okHttpRequest = new OKHttpRequest(methed, url);
        if (Application.getUser() != null) {
            okHttpRequest.setHeader("Authorization", Base64.encodeToString((Application.getUser().getUsername() + ":" + Application.getUser().getPassword()).getBytes(), 0));
            okHttpRequest.setHeader("Cookie", "JSESSIONID=DB89DDEE708E87CB0A5B066FD03792E7.jvm1");
        }
        return okHttpRequest;
    }
}
