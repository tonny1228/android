package works.tonny.mobile.demo6.match;

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

public class MatchViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_view_activity);
        TitleHelper.getInstance(this).enableBack().setTitle("赛事详情");
        new Request().execute(getIntent().getStringExtra("url"));
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(MatchViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            try {
                myDialog.dismiss();
                Log.info(result.get("data.item.title"));
                ActivityHelper.getInstance(MatchViewActivity.this).setText(R.id.mc, (String) result.get("data.list.item.title"))
                        .setText(R.id.rq, (String) result.get("data.list.item.date"))
                        .setText(R.id.jbdw, (String) result.get("data.list.item.jbdw"))
                        .setText(R.id.cbdw, (String) result.get("data.list.item.cbdw"))
                        .setText(R.id.sj, (String) result.get("data.list.item.time"))
                        .setText(R.id.cp, (String) result.get("data.list.item.cp"))
                        .setText(R.id.addr, (String) result.get("data.list.item.addr"));
            } catch (Exception e) {
                Log.error(e);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(MatchViewActivity.this, null);
                this.cancel(true);
            }
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, params[0]);
            String xml = null;
            try {
                xml = request.executeToString();
                Log.info(xml);
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
