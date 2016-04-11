package works.tonny.mobile.demo6.match;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.FormActivity;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.demo6.user.RechargeActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;

/**
 * 申请参赛 参赛 确认页面
 * Created by tonny on 2016/2/1.
 */
public class ApplyConfirmActivity extends FormActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_apply);
        final List<Map<String, Object>> list = (List<Map<String, Object>>) getIntent().getSerializableExtra("list");
        ListView lv = (ListView) findViewById(R.id.list);
        SimpleAdapter adapter = new SimpleAdapter(ApplyConfirmActivity.this, list, R.layout.match_apply_confirm_dog_item,
                new String[]{"cname", "blood", "earid"}, new int[]{R.id.mc, R.id.xtzsh, R.id.eh});
        lv.setAdapter(adapter);
        TitleHelper.getInstance(this).enableBack().setTitle("确认参赛").setButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer b = new StringBuffer();
                for (Map<String, Object> m : list) {
                    b.append(m.get("blood")).append(",");
                }
                if (b.length() > 0) {
                    b.deleteCharAt(b.length() - 1);
                }
                Map<String, Object> params = new HashMap<>();
                params.put("action", "applymatch");
                params.put("coid", getIntent().getStringExtra("id"));
                params.put("bloods", b.toString());
                new Post().execute(params);
            }
        });

    }

    @Override
    protected String getUrl() {
        return "http://www.csvclub.org/servlet/EditCsvClient";
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }


    public class Post extends AsyncTask<Map<String, Object>, Integer, Object> {

        private HttpRequest request;
        private ProgressDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = ProgressDialog.show(ApplyConfirmActivity.this, getActivityTitle(), "提交中", true);
        }

        @Override
        protected void onPostExecute(Object result) {
            try {
                super.onPostExecute(result);
                myDialog.dismiss();
                if (result == null) {
                    Toast.makeText(ApplyConfirmActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> o = (Map<String, Object>) result;
                Log.info(o.get("s2m.body.tag"));
                if (o.get("s2m.body.tag") != null && "success".equals((String) ((Map) o.get("s2m.body.tag")).get("type"))) {
                    Toast.makeText(ApplyConfirmActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    beforeFinished(result);
                    finish();
                    return;
                } else if (o.get("s2m.body.tag") != null) {
                    Toast.makeText(ApplyConfirmActivity.this, (String) ((Map) o.get("s2m.body.tag")).get("value"), Toast.LENGTH_SHORT).show();
                    Log.info(o);
                    setResult(1);
                    finish();
                } else if (o.get("data.list.item") != null) {
                    Map<String, Object> os = new HashMap<>();
                    os.put("data", o.get("data.list.item"));
                    Intent intent = IntentUtils.newInstance(ApplyConfirmActivity.this, RechargeActivity.class, os);
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
                LoginActivity.startLoginActivity(ApplyConfirmActivity.this, null);
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
}
