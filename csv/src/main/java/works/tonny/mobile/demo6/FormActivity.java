package works.tonny.mobile.demo6;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.LayoutUtils;
import works.tonny.mobile.demo6.user.RechargeActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;

/**
 * 表单页抽象类
 * Created by tonny on 2015/8/5.
 */
public abstract class FormActivity extends Activity {


    /**
     * 数据不能为空
     *
     * @param value
     * @param title
     * @return
     */
    protected boolean assertNotNull(String value, String title) {
        if (StringUtils.isEmpty(value)) {
            LayoutUtils.alert(this, title).show();
            return false;
        }
        return true;
    }

    protected boolean assertTrue(boolean value, String title) {
        if (!value) {
            LayoutUtils.alert(this, title).show();
            return false;
        }
        return true;
    }


    public void beforeFinished(Object result) {

    }


    public class Post extends AsyncTask<Map<String, Object>, Integer, Object> {

        private HttpRequest request;
        private ProgressDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = ProgressDialog.show(FormActivity.this, getActivityTitle(), "提交中", true);
        }

        @Override
        protected void onPostExecute(Object result) {
            try {
                super.onPostExecute(result);
                myDialog.dismiss();
                if (result == null) {
                    Toast.makeText(FormActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> o = (Map<String, Object>) result;
                Object message = o.get("s2m.body.tag");
                Log.info(message);
                if (message != null && "success".equals((String) ((Map) o.get("s2m.body.tag")).get("type"))) {
                    Toast.makeText(FormActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    beforeFinished(result);
                    finish();
                    return;
                } else if (o.get("s2m.body.tag") != null) {
                    Toast.makeText(FormActivity.this, (String) ((Map) o.get("s2m.body.tag")).get("value"), Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                    return;
                } else if (o.get("data.list.item") != null) {
                    Map<String, Object> os = new HashMap<>();
                    Map<String, Object> data = (Map<String, Object>) o.get("data.list.item");
                    if (!data.containsKey("alipayId")) {
                        return;
                    }
                    os.put("data", data);
                    Intent intent = IntentUtils.newInstance(FormActivity.this, RechargeActivity.class, os);
                    startActivity(intent);
                    setResult(0);
                    finish();
                }

            } catch (Exception e) {
                Log.error(e);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            myDialog.dismiss();
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(FormActivity.this, null);
                this.cancel(true);
            }
        }

        @Override
        protected Object doInBackground(Map<String, Object>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Post, getUrl());
            Map<String, Object> param = params[0];
            for (String key : param.keySet()) {
                if (param.get(key) instanceof File) {
                    request.addFile(key, (File) param.get(key));
                } else
                    request.setFormParam(key, String.valueOf(param.get(key)));
            }
            String xml = null;
            try {
                xml = request.executeToString();
                XMLParser parser = new XMLParser();
                Log.info("xml(" + request.getResponseCode() + "):" + xml);
                parser.parse(xml);
                return parser.getDatas();
            } catch (HttpRequestException e) {
                e.printStackTrace();
                return null;
            } catch (AuthException e) {
                Log.error(e);
                this.publishProgress(-1);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    protected abstract String getUrl();

    protected abstract String getActivityTitle();
}
