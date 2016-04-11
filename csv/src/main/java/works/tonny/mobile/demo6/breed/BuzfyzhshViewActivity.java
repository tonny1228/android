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

public class BuzfyzhshViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_buzfyzhsh_view);
        TitleHelper.getInstance(this).enableBack().setTitle("补做繁育证书");
        new Request().execute(getIntent().getStringExtra("url"));
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(BuzfyzhshViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                if (values != null && values[0] == -1) {
                    LoginActivity.startLoginActivity(BuzfyzhshViewActivity.this, null);
                    this.cancel(true);
                }
            } catch (Exception e) {
                Log.error(e);
            }
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            try {
                ActivityHelper.getInstance(BuzfyzhshViewActivity.this).setText(R.id.djywm, (String) result.get("data.item.djywm"))
                        .setText(R.id.sfyyj, (String) result.get("data.item.sfyyj"))
                        .setText(R.id.sqsj, (String) result.get("data.item.sqsj"))
                        .setText(R.id.yjsj, (String) result.get("data.item.yjsj"))
                        .setText(R.id.csrq, (String) result.get("data.item.csrq"))
                        .setText(R.id.xb, (String) result.get("data.item.xb"))
                        .setText(R.id.eh, (String) result.get("data.item.eh"))
                        .setText(R.id.gwxtzsh, (String) result.get("data.item.gwxtzsh"))
                        .setText(R.id.fzr, (String) result.get("data.item.fzr"))
                        .setText(R.id.gwjc, (String) result.get("data.item.gwjc"))
                        .setText(R.id.fzrdz, (String) result.get("data.item.fzrdz"))
                        .setText(R.id.ssgj, (String) result.get("data.item.ssgj"))
                        .setText(R.id.fqxtzsh, (String) result.get("data.item.fqxtzsh"))
                        .setText(R.id.mqxtzsh, (String) result.get("data.item.mqxtzsh"))
                        .setText(R.id.sqsj, (String) result.get("data.item.sqsj"))
                        .setText(R.id.yjsj, (String) result.get("data.item.yjsj"));
            } catch (Exception e) {
                Log.error(e);
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
