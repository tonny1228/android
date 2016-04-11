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

public class DNAViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_dna_view);
        TitleHelper.getInstance(this).enableBack().setTitle("DNA检测申请");
        new Request().execute(getIntent().getStringExtra("url"));
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(DNAViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(DNAViewActivity.this, null);
                this.cancel(true);
            }
        }
        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            try {
                myDialog.dismiss();
                Log.info(result.get("data.item.title"));
                ActivityHelper.getInstance(DNAViewActivity.this).setText(R.id.xtzsh, (String) result.get("data.item.title"))
                        .setText(R.id.eh, (String) result.get("data.item.summary"))
                        .setText(R.id.zt, (String) result.get("data.item.date"))
                        .setText(R.id.dnabh, (String) result.get("data.item.dnacode"))
                        .setText(R.id.xybh, (String) result.get("data.item.bloodcode"))
                        .setText(R.id.cxrq, (String) result.get("data.item.drawbloodtime"))
                        .setText(R.id.sbsj, (String) result.get("data.item.applicationtime"))
                        .setText(R.id.sjsj, (String) result.get("data.item.inspecttime"))
                        .setText(R.id.batchnum, (String) result.get("data.item.batchnum"))
                        .setText(R.id.dabh, (String) result.get("data.item.filecode"));
            } catch (Exception e) {
                Log.info(e);
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
