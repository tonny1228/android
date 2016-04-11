package works.tonny.mobile.demo6.query;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.DataView;
import works.tonny.mobile.widget.IDLinkedHashMap;
import works.tonny.mobile.widget.LoadingDialog;

public class TestActivity extends Activity {

    private DataView listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_qzcx_frag);

        new RequestBg().execute("http://www.csvclub.org/jsp/csvclub/csvclient/search/csvdogmatch.jsp?blood=CSZ0006365");
    }


    /**
     * 载入公告
     *
     * @param fragmentTransaction
     * @param datas
     */
    private void loadNews(FragmentTransaction fragmentTransaction, ArrayList<? extends Map<String, ?>> datas) {
        listFragment = DataView.newInstance(datas, R.layout.index_news_item);
        listFragment.setMapping(new SsxxbActivity().getMapping());
        fragmentTransaction.replace(R.id.list, listFragment);
    }


    public class RequestBg extends AsyncTask<String, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(TestActivity.this);
            myDialog.show();
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            List<IDLinkedHashMap> item = (List<IDLinkedHashMap>) result.get("data.list.item");
            Log.info(result);
            Log.info(item);
//            listFragment.refresh(item);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            loadNews(fragmentTransaction, (ArrayList<? extends Map<String, ?>>) item);
            fragmentTransaction.commit();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(TestActivity.this, null);
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
