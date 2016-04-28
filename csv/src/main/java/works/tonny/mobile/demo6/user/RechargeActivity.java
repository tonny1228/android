package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Calendar;
import java.util.HashMap;
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

public class RechargeActivity extends Activity {

    private Calendar sj = Calendar.getInstance();
    private Calendar instance;
    private ActivityHelper activityHelper;
    private String alipayId;
    private String catalogId;
    private String year;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_recharge);
        activityHelper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("会员续费").setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = activityHelper.getValue(R.id.je);

                if (NumberUtils.toInt(value) < 1) {
                    Toast.makeText(RechargeActivity.this, "金额不能为负数", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("action", "paying");
                map.put("catalogId", catalogId);
                map.put("alipayId", alipayId);
                map.put("year", year);
                map.put("balance", value);
                map.put("paymentType", "2");
                map.put("info", activityHelper.getValue(R.id.bz));
                new Post().execute(map);
            }
        });

//        final EditText zzsj = (EditText) findViewById(R.id.zzsj);
//        zzsj.setOnClickListener(new PickDate(zzsj, sj));
//        instance = Calendar.getInstance();
//        activityHelper.makeItDate(R.id.zzsj, instance);
//        final Spinner spinner = (Spinner) findViewById(R.id.year);

//        int year = sj.get(Calendar.YEAR);
//        String[] years = new String[5];
//        for (int i = 0; i < 5; i++) {
//            years[i] = String.valueOf(year - 1 + i);
//        }

////        Spinner s = (Spinner) findViewById(R.id.feiyong);
//        Log.info(s.getAdapter());
//        Log.info(android.R.layout.simple_spinner_item);

        Map<String, Object> data = (Map<String, Object>) getIntent().getSerializableExtra("data");
        if (data == null) {
            new Get().execute();
        } else {
            init(data);
        }


    }

    private void init(Map map) {
        activityHelper.setText(R.id.feiyong, (String) map.get("catalogName")).setText(R.id.je, StringUtils.substringBefore((String) map.get("balance"), "."));
        catalogId = (String) map.get("catalogId");
        money = (String) map.get("balance");
        alipayId = (String) map.get("alipayId");
        year = (String) map.get("year");
    }

    class Get extends AsyncTask<Map<String, String>, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog loadingDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = LoadingDialog.newInstance(RechargeActivity.this);
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            loadingDialog.dismiss();
            Log.info(result.keySet());
            Map map = (Map) result.get("data.list.item");
            if (map == null && result.get("s2m.body.tag") != null) {
                Toast.makeText(RechargeActivity.this, (String) ((Map) result.get("s2m.body.tag")).get("value"), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
//            Log.info(map);
            init(map);
        }

        @Override
        protected Map<String, Object> doInBackground(Map<String, String>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_user_recharge_before));

            String xml = null;
            try {
                xml = request.executeToString();
                XMLParser xmlParser = new XMLParser();
                xmlParser.parse(xml);
//                Log.info(xml);
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
            myDialog = ProgressDialog.show(RechargeActivity.this, "会员续费", "提交中", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            if (result == null) {
                LoginActivity.startLoginActivity(RechargeActivity.this, null);
                return;
            }
            Toast.makeText(RechargeActivity.this, "提交" + (result ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
            Intent intent = IntentUtils.newInstance(getApplicationContext(), AlipayActivity.class, "subject", "会员续费", "body", "会员续费", "price", money, "no", alipayId);
            startActivity(intent);
            if (result) {
                finish();
            }

        }

        @Override
        protected Boolean doInBackground(Map<String, String>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Post, Application.getUrl(R.string.url_user_recharge));
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
