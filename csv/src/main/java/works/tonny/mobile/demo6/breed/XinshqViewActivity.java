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

public class XinshqViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_xinshq_view);
        TitleHelper.getInstance(this).enableBack().setTitle("新生犬证明");
        new Request().execute(getIntent().getStringExtra("url"));
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(XinshqViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            try {
                myDialog.dismiss();
//            ActivityHelper.getInstance(XinshqViewActivity.this).setText(R.id.pqbh, (String) result.get("data.item.pqbh"))
//                    .setText(R.id.xinshq_date, (String) result.get("data.item.xinshq_date"))
//                    .setText(R.id.csqz_g, (String) result.get("data.item.csqz_g"))
//                    .setText(R.id.csqz_m, (String) result.get("data.item.csqz_m"))
//                    .setText(R.id.st_g, (String) result.get("data.item.st_g"))
//                    .setText(R.id.st_m, (String) result.get("data.item.st_m"))
//                    .setText(R.id.swyq_g, (String) result.get("data.item.swyq_g"))
//                    .setText(R.id.swyq_m, (String) result.get("data.item.swyq_m"))
//                    .setText(R.id.sqqs_g, (String) result.get("data.item.sqqs_g"))
//                    .setText(R.id.sqqs_m, (String) result.get("data.item.sqqs_m"));
                ActivityHelper.getInstance(XinshqViewActivity.this).setText(R.id.pqbh, (String) result.get("data.item.title"))
                        .setText(R.id.pqrq, (String) result.get("data.item.date"))
                        .setText(R.id.csrq, (String) result.get("data.item.birthdate"))
                        .setText(R.id.gqzsh, (String) result.get("data.item.fblood"))
                        .setText(R.id.qzs, (String) result.get("data.item.num"))
                        .setText(R.id.mqzsh, (String) result.get("data.item.mblood"))
                        .setText(R.id.sfkw, (String) result.get("data.item.isempty"))
                        .setText(R.id.qzzs, (String) result.get("data.item.dognums"));
            } catch (Exception e) {
                Log.error(e);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(XinshqViewActivity.this, null);
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
