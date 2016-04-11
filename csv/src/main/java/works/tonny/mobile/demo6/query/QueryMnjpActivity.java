package works.tonny.mobile.demo6.query;

import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;


public class QueryMnjpActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return "模拟交配查询";
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_query_jpcx).replace("${fkey}",getIntent().getStringExtra("jpgqzsh"))//)
                .replace("${mkey}", getIntent().getStringExtra("jpmqzsh"));// );
    }

    @Override
    protected int getLayout() {
        return R.layout.query_result_list;
    }

    @Override
    protected String getResultDataName() {
        return "data.item";
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return null;
    }


    @Override
    protected int getItemLayout() {
        return R.layout.query_mnjp_item;
    }


    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("bloodf", R.id.fblood);
        mapping.put("bloodm", R.id.mblood);
        mapping.put("show", R.id.show);
        return mapping;
    }

}
