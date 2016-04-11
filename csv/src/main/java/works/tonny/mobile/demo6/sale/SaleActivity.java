package works.tonny.mobile.demo6.sale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.CartActivity;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;

/**
 * Created by tonny on 2015/8/11.
 */
public class SaleActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return "商城";
    }

    @Override
    protected boolean isAddEnabled() {
        return true;
    }

    @Override
    protected Class getAddActivityClass() {
        return CartActivity.class;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.sale_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_sale);
    }

    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("image", R.id.list_item_image);
        mapping.put("title", R.id.list_item_title);
        mapping.put("no", R.id.sale_no);
        mapping.put("kc", R.id.sale_kc);
        mapping.put("cjl", R.id.sale_cjl);
        mapping.put("scjg", R.id.sale_scjg);
        mapping.put("hyj", R.id.sale_hyj);
        return mapping;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected String getButtonText() {
        return null;
    }


    @Override
    protected int getButtonIcon() {
        return R.drawable.icon_cart;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(SaleActivity.this, SaleItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

    @Override
    protected boolean loadMore() {
        return true;
    }

}
