package works.tonny.mobile.http;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Map;

import works.tonny.mobile.R;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.LoadingDialog;

/**
 * XML请求任务
 * Created by tonny on 2015/7/10.
 */
public class RequestTask extends AsyncTask<String, Integer, Map<String, Object>> {

    private HttpRequest request;
    private LoadingDialog loadingDialog;
    private Context context;
    private Requested requested;

    public RequestTask(Context context, Requested requested) {
        this.context = context;
        this.requested = requested;
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadingDialog = LoadingDialog.newInstance(context);
        loadingDialog.show();
    }

    @Override
    protected void onPostExecute(Map<String, Object> result) {
        super.onPostExecute(result);
        loadingDialog.dismiss();
        if (requested != null) {
            requested.execute(result);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    public static interface Requested {
        /**
         * 请求结束后对数据进行处理
         *
         * @param map
         */
        void execute(Map<String, Object> map);
    }
}
