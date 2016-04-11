package works.tonny.mobile.demo6.user;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.demo6.query.QzcxViewActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.DataView;
import works.tonny.mobile.widget.IDLinkedHashMap;
import works.tonny.mobile.widget.LoadingDialog;

/**
 * Created by tonny on 2016/1/30.
 */
public class NearbyQuansheViewActivity extends Activity {
    private ActivityHelper helper;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_quanshe_view);
        TitleHelper.getInstance(this).setTitle("犬舍").enableBack();
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
            myDialog = LoadingDialog.newInstance(NearbyQuansheViewActivity.this);
            myDialog.show();
        }

        @Override
        protected void onPostExecute(final Map<String, Object> result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            Object other = result.get("s2m.body.tag");
            if (other != null && "error".equals(((Map) other).get("type"))) {
                Toast.makeText(NearbyQuansheViewActivity.this, (CharSequence) ((Map) other).get("title"), Toast.LENGTH_SHORT).show();
                return;
            }

            helper.setImage(R.id.image, (String) result.get("data.baseinfo.img")).setText(R.id.zwm, (String) result.get("data.baseinfo.name"))
                    .setText(R.id.linkman, (String) result.get("data.baseinfo.linkman"))
                    .setText(R.id.address, (String) result.get("data.baseinfo.address"))
                    .setText(R.id.phone, (String) result.get("data.baseinfo.phone")).setText(R.id.info, (String) result.get("data.baseinfo.info"))
            ;
            helper.setOnClickListener(R.id.location, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("cur_latitude", getIntent().getStringExtra("cur_latitude"));
                    item.put("cur_longitude", getIntent().getStringExtra("cur_longitude"));
                    item.put("latitude", result.get("data.baseinfo.latitude"));
                    item.put("longitude", result.get("data.baseinfo.longitude"));
                    Intent intent = IntentUtils.newInstance(NearbyQuansheViewActivity.this, BaiduMapActivity.class, item);
                    startActivity(intent);
                }
            });


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            final List<IDLinkedHashMap> datas = new ArrayList<>();
            String phone = (String) result.get("data.baseinfo.phone");
            String[] no = phone.split(",");
            for (String s : no) {
                IDLinkedHashMap m = new IDLinkedHashMap();
                m.put("phone", s.trim());
                datas.add(m);
            }
            DataView dataView = DataView.newInstance(datas, works.tonny.mobile.R.layout.layout_phone_item);
            Map<String, Integer> mapping1 = new HashMap<String, Integer>();
            mapping1.put("phone", works.tonny.mobile.R.id.phone);
            dataView.setMapping(mapping1);
            dataView.setItemClickListener(new DataView.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + datas.get(position).get("phone")));
                    startActivity(intent);
                }
            });
            fragmentTransaction.replace(R.id.phone, dataView);


            final List<Map> data = new ArrayList<Map>();
            Object o = result.get("data.list.item");
            if (o instanceof Map) {
                data.add((Map) o);
            } else {
                data.addAll((Collection<? extends Map>) o);
            }
            DataView inList = DataView.newInstance(data, R.layout.user_quanshe_item);
            Map<String, Integer> mapping = new HashMap<String, Integer>();
            mapping.put("img", R.id.user_head);
            mapping.put("name", R.id.name);
            mapping.put("blood", R.id.blood);
            mapping.put("sex", R.id.sex);

            mapping.put("date", R.id.date);
            inList.setMapping(mapping);
            inList.setItemClickListener(new DataView.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", data.get(position).get("url").toString());
                    startActivity(IntentUtils.newInstance(NearbyQuansheViewActivity.this, QzcxViewActivity.class, bundle));
                }
            });
            fragmentTransaction.replace(R.id.list, inList);


            fragmentTransaction.commitAllowingStateLoss();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values != null && values[0] == -1) {
                LoginActivity.startLoginActivity(NearbyQuansheViewActivity.this, null);
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
