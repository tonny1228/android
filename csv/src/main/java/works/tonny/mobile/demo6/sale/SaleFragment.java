package works.tonny.mobile.demo6.sale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.CartActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.IDLinkedHashMap;
import works.tonny.mobile.widget.ListFragment;


/**

 */
public class SaleFragment extends Fragment {
    private ListFragment fragment;

    public SaleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_list, container, false);
        TitleHelper.getInstance(view).setTitle("商城").setButton(R.drawable.icon_cart, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = ListFragment.newInstance(getClass().getName(), R.layout.sale_item, true);
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("image", R.id.list_item_image);
        mapping.put("title", R.id.list_item_title);
        mapping.put("no", R.id.sale_no);
        mapping.put("kc", R.id.sale_kc);
        mapping.put("cjl", R.id.sale_cjl);
        mapping.put("scjg", R.id.sale_scjg);
        mapping.put("hyj", R.id.sale_hyj);
        fragment.setMapping(mapping);
        fragmentTransaction.replace(R.id.list, fragment);
        fragmentTransaction.commit();
        Log.info("ccccccccccccccccccccccccccccc");

        fragment.setRefreshListener(new ListFragment.OnRefreshListener() {
            private HttpRequest request;

            @Override
            public Object refresh(List<IDLinkedHashMap> data) {
                request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_sale));
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


        fragment.setLoadMoreListener(new ListFragment.OnLoadMoreListener() {
            private HttpRequest request;

            @Override
            public List<IDLinkedHashMap> loadMore(List<IDLinkedHashMap> data) {
                request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_sale));
                try {
                    XMLParser xmlParser = new XMLParser();
                    String xml = request.executeToString();
                    xmlParser.parse(xml);
                    Map<String, Object> datas = xmlParser.getDatas();
                    request = null;
                    return (List<IDLinkedHashMap>) datas.get("data.list.item");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });


        fragment.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(container.getContext(), SaleItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        fragment.refresh();
    }

}
