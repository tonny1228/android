package works.tonny.mobile.demo6.query;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.demo6.user.BaiduMapActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.DataView;
import works.tonny.mobile.widget.IDLinkedHashMap;
import works.tonny.mobile.widget.LoadingDialog;

public class QzcxViewActivity extends Activity {

    private ActivityHelper helper;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_qzcx_view);
        TitleHelper.getInstance(this).setTitle("犬只查询").enableBack();
        url = getIntent().getStringExtra("url");
        new Request().execute(url);
        helper = ActivityHelper.getInstance(this);

    }

    class Request extends AsyncTask<String, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(QzcxViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onPostExecute(final Map<String, Object> result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            Object other = result.get("s2m.body.tag");
            if (other != null && "error".equals(((Map) other).get("type"))) {
                Toast.makeText(QzcxViewActivity.this, (CharSequence) ((Map) other).get("title"), Toast.LENGTH_SHORT).show();
                return;
            }

            helper.setImage(R.id.image, (String) result.get("data.baseinfo.img")).setText(R.id.zwm, (String) result.get("data.baseinfo.cname"))
                    .setText(R.id.ywm, (String) result.get("data.baseinfo.ename"))
                    .setText(R.id.eh, (String) result.get("data.baseinfo.earid"))
                    .setText(R.id.xtzsh, (String) result.get("data.baseinfo.blood"))
                    .setText(R.id.xph, (String) result.get("data.baseinfo.chipid"))
                    .setText(R.id.xb, (String) result.get("data.baseinfo.sex"))
                    .setText(R.id.csrq, (String) result.get("data.baseinfo.birthdate"))
                    .setText(R.id.dna, (String) result.get("data.baseinfo.dna"))
                    .setText(R.id.kzjd, (String) result.get("data.baseinfo.hip"))
                    .setText(R.id.examgrade, (String) result.get("data.baseinfo.examgrade"))
                    .setText(R.id.fblood, (String) result.get("data.baseinfo.fblood"))
                    .setText(R.id.address, (String) result.get("data.baseinfo.address"))
                    .setText(R.id.member, (String) result.get("data.baseinfo.member"))
                    .setText(R.id.breedpeople, (String) result.get("data.baseinfo.breedpeople"));
            final String cur_latitude = getIntent().getStringExtra("cur_latitude");
            final String cur_longitude = getIntent().getStringExtra("cur_longitude");
            if (cur_latitude == null || cur_longitude == null) {
                helper.setVisible(R.id.addr, false);
            }
            helper.setOnClickListener(R.id.location, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> item = new HashMap<String, Object>();

                    if (cur_latitude == null || cur_longitude == null) {
                        return;
                    }
                    item.put("cur_latitude", cur_latitude);
                    item.put("cur_longitude", cur_longitude);
                    item.put("latitude", result.get("data.baseinfo.latitude"));
                    item.put("longitude", result.get("data.baseinfo.longitude"));
                    Intent intent = IntentUtils.newInstance(QzcxViewActivity.this, BaiduMapActivity.class, item);
                    startActivity(intent);
                }
            });

            String phone = (String) result.get("data.baseinfo.phone");
            if (phone != null) {

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                final List<IDLinkedHashMap> data = new ArrayList<>();

                String[] no = phone.split(",");
                for (String s : no) {
                    IDLinkedHashMap m = new IDLinkedHashMap();
                    m.put("phone", s.trim());
                    data.add(m);
                }
                DataView dataView = DataView.newInstance(data, works.tonny.mobile.R.layout.layout_phone_item);
                Map<String, Integer> mapping = new HashMap<String, Integer>();
                mapping.put("phone", works.tonny.mobile.R.id.phone);
                dataView.setMapping(mapping);
                dataView.setItemClickListener(new DataView.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.get(position).get("phone")));
                        startActivity(intent);
                    }
                });
                fragmentTransaction.replace(R.id.phone, dataView);
                fragmentTransaction.commit();

            }
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("bloodurl", (String) result.get("data.urls.bloodurl"));
            map.put("match", (String) result.get("data.urls.match"));
            map.put("prove", (String) result.get("data.urls.prove"));
            map.put("posterity", (String) result.get("data.urls.posterity"));
            map.put("tongtai", (String) result.get("data.urls.tongtai"));


            Log.info(url);
            if (map.get("bloodurl") != null && map.get("bloodurl").equals(url)) {
                new RequestBg().execute("bloodurl", url, result);
            }
            if (map.get("match") != null && map.get("match").equals(url)) {
                new RequestBg().execute("match", url, result);
            }
            if (map.get("prove") != null && map.get("prove").equals(url)) {
                new RequestBg().execute("prove", url, result);
            }
            if (map.get("posterity") != null && map.get("posterity").equals(url)) {
                new RequestBg().execute("posterity", url, result);
            }
            if (map.get("tongtai") != null && map.get("tongtai").equals(url)) {
                new RequestBg().execute("tongtai", url, result);
            }

            helper.setOnClickListener(R.id.xtxxb, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object o = result.get("data.urls.bloodurl");
                    new RequestBg().execute("bloodurl", (String) o, result);
                }
            });
            helper.setOnClickListener(R.id.ssxxb, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object o = result.get("data.urls.match");
                    new RequestBg().execute("match", (String) o, result);
                }
            });
            helper.setOnClickListener(R.id.pq, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object o = result.get("data.urls.prove");
                    new RequestBg().execute("prove", (String) o, result);
                }
            });
            helper.setOnClickListener(R.id.hdq, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object o = result.get("data.urls.posterity");
                    new RequestBg().execute("posterity", (String) o, result);
                }
            });
            helper.setOnClickListener(R.id.ttq, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object o = result.get("data.urls.tongtai");
                    new RequestBg().execute("tongtai", (String) o, result);
                }
            });
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(QzcxViewActivity.this, null);
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


    public class RequestBg extends AsyncTask<Object, Integer, Map<String, Object>> {

        private HttpRequest request;
        private LoadingDialog myDialog;
        private String type;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = LoadingDialog.newInstance(QzcxViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onPostExecute(Map<String, Object> result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            Log.info(result);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (type.equals("bloodurl")) {
                fragmentTransaction.replace(R.id.switch_, XtxxbActivity.newInstance(result));
            }
            if (type.equals("match")) {
                fragmentTransaction.replace(R.id.switch_, SsxxbActivity.newInstance(result));
            }
            if (type.equals("prove")) {
                fragmentTransaction.replace(R.id.switch_, PeiquanActivity.newInstance(result));
            }
            if (type.equals("posterity")) {
                fragmentTransaction.replace(R.id.switch_, HdqActivity.newInstance(result));
            }
            if (type.equals("tongtai")) {
                fragmentTransaction.replace(R.id.switch_, TtqActivity.newInstance(result));
            }
            fragmentTransaction.commit();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(QzcxViewActivity.this, null);
                this.cancel(true);
            }
        }

        @Override
        protected Map<String, Object> doInBackground(Object... params) {
            type = (String) params[0];
            Object u = params[1];
            if (url.equals(u)) {
                return (Map<String, Object>) params[2];
            }
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, (String) u);
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
