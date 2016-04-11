package works.tonny.mobile.demo6.paihang;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.query.QzcxViewActivity;

public class HjPahangActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected String getUrl() {
        return getIntent().getStringExtra("url");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }


    @Override
    protected int getItemLayout() {
        return R.layout.pahang_activity_hj;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("url", (String) item.get("url"));
                startActivity(IntentUtils.newInstance(HjPahangActivity.this, QzcxViewActivity.class, bundle));
            }
        };
    }


    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("title", R.id.title);
        mapping.put("summary", R.id.summary);
        mapping.put("date", R.id.date);
        return mapping;
    }

    @Override
    protected String getResultDataName() {
        return "data.hjph.item";
    }
}
