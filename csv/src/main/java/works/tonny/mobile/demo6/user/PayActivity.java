package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.demo6.pay.AlipayActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.LoadingDialog;

public class PayActivity extends Activity {
    private Calendar sj = Calendar.getInstance();
    private String price;
    private String ssxm;
    private Map<String, String> types = new HashMap<String, String>();
    private String alipayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_pay);
        final ActivityHelper activityHelper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("支付宝充值").setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = activityHelper.getValue(R.id.jfje);

                if (StringUtils.isEmpty(price) || Float.parseFloat(price) <= 0) {
                    Toast.makeText(PayActivity.this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("action", "recharge");
                map.put("alipayId", alipayId);
                ssxm = activityHelper.getValue(R.id.ssxm);
                map.put("catalogId", types.get(ssxm));
                map.put("balance", activityHelper.getValue(R.id.jfje));
                map.put("info", activityHelper.getValue(R.id.bz));
                new Post().execute(map);
            }
        });
        new Get().execute();

    }


    class Get extends AsyncTask<Map<String, String>, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog loadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = LoadingDialog.newInstance(PayActivity.this);
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            loadingDialog.dismiss();
            Object item = result.get("data.list.item");
            if (item == null && result.get("s2m.body.tag") != null) {
                Toast.makeText(PayActivity.this, (String) ((Map) result.get("s2m.body.tag")).get("value"), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            Spinner spinner = (Spinner) findViewById(R.id.ssxm);

            List<String> list = new ArrayList<String>();
            if (item instanceof Map) {
                Map item1 = (Map) item;
                list.add((String) item1.get("catalogName"));
                types.put((String) item1.get("catalogName"), (String) item1.get("catalogId"));
            } else if (item instanceof List) {
                List<Map<String, Object>> l = (List<Map<String, Object>>) item;
                for (Map<String, Object> item1 : l) {
                    list.add((String) item1.get("catalogName"));
                    types.put((String) item1.get("catalogName"), (String) item1.get("catalogId"));
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(PayActivity.this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            alipayId = (String) result.get("data.alipayId");
        }

        @Override
        protected Map<String, Object> doInBackground(Map<String, String>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_user_pay_before));

            String xml = null;
            try {
                xml = request.executeToString();
                XMLParser xmlParser = new XMLParser();
                xmlParser.parse(xml);
                return (Map<String, Object>) xmlParser.getDatas();
            } catch (HttpRequestException e) {
                Log.error(e);
                return null;
            } catch (AuthException e) {
                Log.error(e);
                return null;
            } catch (Exception e) {
                Log.error(e);
                return null;
            }
        }
    }


    class Post extends AsyncTask<Map<String, String>, Integer, Boolean> {

        private HttpRequest request;
        private ProgressDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = ProgressDialog.show(PayActivity.this, "会员续费", "提交中", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            if (result == null) {
                LoginActivity.startLoginActivity(PayActivity.this, null);
                return;
            }
            Toast.makeText(PayActivity.this, "提交" + (result ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
            if (result) {
                Intent intent = IntentUtils.newInstance(getApplicationContext(), AlipayActivity.class, "subject", ssxm, "body", ssxm, "price", price, "no", alipayId);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Map<String, String>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Post, Application.getUrl(R.string.url_user_pay));
            Map<String, String> param = params[0];
            for (String key : param.keySet()) {
                request.setFormParam(key, param.get(key));
            }
            String xml = null;
            try {
                xml = request.executeToString();
            } catch (HttpRequestException e) {
                e.printStackTrace();
                return false;
            } catch (AuthException e) {
                Log.error(e);
                return null;
            }
//            XMLParser xmlParser = new XMLParser();
//            xmlParser.parse(xml);
//            xmlParser.getDatas()
            return xml.contains("success");
        }
    }

}
