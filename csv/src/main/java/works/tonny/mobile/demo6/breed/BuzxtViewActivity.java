package works.tonny.mobile.demo6.breed;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.LoadingDialog;

public class BuzxtViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_buzxt_view);
        TitleHelper.getInstance(this).enableBack().setTitle("补做血统证书");
        new Request().execute(getIntent().getStringExtra("url"));
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(BuzxtViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            try {
                myDialog.dismiss();
                ActivityHelper.getInstance(BuzxtViewActivity.this).setText(R.id.eh, (String) result.get("data.item.eh"))
                        .setText(R.id.xtzsh, (String) result.get("data.item.xtzsh"))
                        .setText(R.id.yy, (String) result.get("data.item.yy"));
            } catch (Exception e) {
                Log.error(e);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(BuzxtViewActivity.this, null);
                this.cancel(true);
            }
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, params[0]);
            String xml = null;
            try {
                xml = request.executeToString();
                XMLParser xmlParser = new XMLParser();
                xmlParser.parse(xml);
                return xmlParser.getDatas();
            } catch (AuthException e) {
                e.printStackTrace();
                this.publishProgress(-1);
                return null;
            } catch (HttpRequestException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
