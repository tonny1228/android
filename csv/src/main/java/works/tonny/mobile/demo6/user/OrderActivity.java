package works.tonny.mobile.demo6.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.widget.IDLinkedHashMap;

/**
 * Created by tonny on 2015/7/22.
 */
public class OrderActivity extends ListActivity {
    @Override
    protected String getListTitle() {
        return "我的订单";
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_user_order);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IDLinkedHashMap item = (IDLinkedHashMap) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(OrderActivity.this, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", item.get("url").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("title", R.id.ddh);
        mapping.put("je", R.id.je);
        mapping.put("date", R.id.date);
        mapping.put("shr", R.id.shr);
        mapping.put("zt", R.id.zt);
        return mapping;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.user_order_item;
    }


    @Override
    protected boolean loadMore() {
        return true;
    }


}