package works.tonny.mobile.demo6.query;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.utils.Log;


public class QueryQzcxActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return "犬只信息查询";
    }

    @Override
    protected String getUrl() {
        String replace = Application.getUrl(R.string.url_query_qzcx).replace("${name}", getIntent().getStringExtra("name"))//
                .replace("${checktype}", String.valueOf(getIntent().getIntExtra("checktype", 0)));
        Log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" + replace);
        return replace;

    }

    @Override
    protected int getLayout() {
        return R.layout.query_result_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Intent intent = IntentUtils.newInstance(QueryQzcxActivity.this.getBaseContext(), QzcxViewActivity.class, item);
                startActivity(intent);

            }
        };
    }


    @Override
    protected int getItemLayout() {
        return R.layout.query_result_item;
    }


    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("blood", R.id.mc);
        mapping.put("cname", R.id.zwm);
        mapping.put("ename", R.id.ywm);
        mapping.put("earid", R.id.eh);
        mapping.put("status", R.id.zt);
        mapping.put("member", R.id.member);
        return mapping;
    }
}
