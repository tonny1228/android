package works.tonny.mobile;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test(){
        HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get,"http://192.168.0.18/moblieInhabitant/verifypassword.action?password=19910611");
        try {
            String s = request.executeToString();
            Log.i("tonny",s);
            System.out.println(s);
        } catch (HttpRequestException e) {
            Log.e("tonny",e.getMessage());
        }catch (AuthException e) {
            works.tonny.mobile.utils.Log.error(e);
//            return null;
        }
    }


    public void testXML(){
        HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get,"http://192.168.0.18/test/list.jsp").useCookieManager()
                .addHeader("Cookie", "u=MTgzMDk4MjM1NjU6MTk5MTA2MTE6MjZhNjEwMjIwMThlNGU2NzgzNzcxYTVjZDczYzA0YTE=");
        try {
           // XMLParser.pase(new ByteArrayInputStream(request.executeToBytes()));
        } catch (Exception e) {
            Log.e("tonny", e.getMessage());
        }
    }
}