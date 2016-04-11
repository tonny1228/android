package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.RequestTask;
import works.tonny.mobile.widget.IDLinkedHashMap;
import works.tonny.mobile.widget.ListFragment;

public class OrderDetailActivity extends Activity {

    private ListFragment list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_order_detail);
        TitleHelper.getInstance(this).enableBack().setTitle("订单详情");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        list = ListFragment.newInstance("cart", R.layout.user_order_detail_item, false);
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("image", works.tonny.mobile.R.id.list_item_image);
        mapping.put("title", works.tonny.mobile.R.id.list_item_title);
        mapping.put("hyj", R.id.cart_jg);
        mapping.put("date", works.tonny.mobile.R.id.list_item_date);
        mapping.put("num", R.id.cart_num);
        list.setMapping(mapping);
        fragmentTransaction.replace(R.id.list_items, list);
        fragmentTransaction.commit();

        new RequestTask(this, new RequestTask.Requested() {
            @Override
            public void execute(Map<String, Object> result) {
                ActivityHelper.getInstance(OrderDetailActivity.this).setText(R.id.ddh, (String) result.get("data.item.title"))
                        .setText(R.id.shr, (String) result.get("data.item.shr"))
                        .setText(R.id.je, (String) result.get("data.item.je"))
                        .setText(R.id.date, (String) result.get("data.item.date")).setText(R.id.zt, (String) result.get("data.item.zt"));

                list.appendDatas((List<IDLinkedHashMap>) result.get("data.item.goods.item"));
                View view = findViewById(R.id.list_items);
                view.getLayoutParams().height = 200 * list.size();
            }
        }).execute(getIntent().getStringExtra("url"));
    }

}
