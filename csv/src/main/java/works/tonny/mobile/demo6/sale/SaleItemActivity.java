package works.tonny.mobile.demo6.sale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.demo6.CartActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;


public class SaleItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_item_detail);
        TitleHelper.getInstance(this).enableBack().setTitle("商品详情");
        final Map<String, Object> item = (Map<String, Object>) getIntent().getSerializableExtra("data");

        ActivityHelper instance = ActivityHelper.getInstance(this);
        instance.setText(R.id.sale_title, (String) item.get("title"));
        instance.setImage(R.id.sale_image, (String) item.get("image"));
        instance.setText(R.id.sale_price, "￥" + (String) item.get("hyj"));
        instance.setText(R.id.sale_no, (String) item.get("no"));
        instance.setText(R.id.sale_date, (String) item.get("date"));
        instance.setText(R.id.sale_level, (String) item.get("level"));
        instance.setText(R.id.sale_kc, (String) item.get("kc"));
        instance.setText(R.id.sale_cjl, (String) item.get("cjl"));
        instance.setText(R.id.sale_scjg, (String) item.get("scjg"));
        instance.setOnClickListener(R.id.sale_cart, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SaleItemActivity.this, CartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
