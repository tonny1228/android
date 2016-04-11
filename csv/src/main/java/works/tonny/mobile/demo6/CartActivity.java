package works.tonny.mobile.demo6;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.IDLinkedHashMap;
import works.tonny.mobile.widget.ListFragment;
import works.tonny.mobile.widget.LoadingDialog;


public class CartActivity extends Activity {
    private ListFragment fragment;
    private boolean loaed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        fragment = ListFragment.newInstance("cart", R.layout.sale_cart_item, true);
        TitleHelper.getInstance(this).setTitle("购物车").enableBack().setButton("结算", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object>[] map = new HashMap[fragment.size()];
                for (int i = 0; i < fragment.size(); i++) {
                    map[i] = new HashMap<String, Object>();
                    map[i].put("id", (fragment.data(i)).get("id"));
                    map[i].put("num", (fragment.data(i)).get("num"));
                    Log.info(map[i]);
                }
                new Post().execute(map);

            }
        });
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("image", works.tonny.mobile.R.id.list_item_image);
        mapping.put("title", works.tonny.mobile.R.id.list_item_title);
        mapping.put("summary", works.tonny.mobile.R.id.list_item_summary);
        mapping.put("jg", works.tonny.mobile.R.id.list_item_date);
        mapping.put("num", R.id.cart_num);
        mapping.put("_index", R.id.cart_index);


        View.OnClickListener add = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) ((ViewGroup) v.getParent()).findViewById(R.id.cart_num);
                int value = Integer.parseInt(text.getText().toString()) + 1;
                text.setText(String.valueOf(value));
                EditText idx = (EditText) ((ViewGroup) v.getParent()).findViewById(R.id.cart_index);
                fragment.data(Integer.parseInt(idx.getText().toString())).put("num", value);
            }
        };

        View.OnClickListener cut = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) ((ViewGroup) v.getParent()).findViewById(R.id.cart_num);
                int i = Integer.parseInt(text.getText().toString());
                if (i > 1)
                    text.setText(String.valueOf(i - 1));
                EditText idx = (EditText) ((ViewGroup) v.getParent()).findViewById(R.id.cart_index);
                fragment.data(Integer.parseInt(idx.getText().toString())).put("num", i);

            }
        };

        View.OnClickListener delete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idx = (EditText) ((ViewGroup) v.getParent()).findViewById(R.id.cart_index);
                int location = Integer.parseInt(idx.getText().toString());
                fragment.remove(location);
            }
        };
        fragment.setViewClickListener(R.id.cart_add, add);
        fragment.setViewClickListener(R.id.cart_num_left, cut);
        fragment.setViewClickListener(R.id.cart_delete, delete);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setMapping(mapping);
        fragmentTransaction.replace(R.id.list, fragment);

        fragmentTransaction.commit();
        fragment.setRefreshListener(new ListFragment.OnRefreshListener() {
            private HttpRequest request;

            @Override
            public Object refresh(List<IDLinkedHashMap> data) {
                request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_sale_cart));
                try {
                    XMLParser xmlParser = new XMLParser();
                    String xml = request.executeToString();
                    xmlParser.parse(xml);
                    Map<String, Object> datas = xmlParser.getDatas();
                    request = null;
                    return datas.get("data.list.item");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void cancelRefresh() {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!loaed) {
            fragment.refresh();
            loaed = true;
        }
    }


    class Post extends AsyncTask<Map<String, Object>, Integer, Boolean> {
        private LoadingDialog loadingDialog;
        private HttpRequest request;

        @Override
        protected void onPostExecute(Boolean v) {
            loadingDialog.dismiss();
            if (v == null) {
                LoginActivity.startLoginActivity(CartActivity.this, null);
                return;
            }
            if (v) {
                Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT);
            }
            finish();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = LoadingDialog.newInstance(CartActivity.this);
            loadingDialog.show();
        }


        @Override
        protected Boolean doInBackground(Map<String, Object>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Post, Application.getUrl(R.string.url_sale_cart_submit));
            for (int i = 0; i < params.length; i++) {
                Map<String, Object> param = params[i];
                for (String key : param.keySet()) {
                    request.addFormParam(key, String.valueOf(param.get(key)));
                }
            }
            String xml = null;
            try {
                xml = request.executeToString();
            } catch (HttpRequestException e) {
                Log.error(e);
                return false;
            } catch (AuthException e) {
                Log.error(e);
                return null;
            }
            return xml.contains("success");
        }
    }

}
