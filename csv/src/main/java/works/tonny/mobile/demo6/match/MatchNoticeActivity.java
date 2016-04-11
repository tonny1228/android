package works.tonny.mobile.demo6.match;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;


public class MatchNoticeActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return getIntent().getStringExtra("title") == null ? "赛事预告" : getIntent().getStringExtra("title");
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_match_list);
    }

    @Override
    protected int getLayout() {
        return R.layout.match_list;
    }

    @Override
    protected int getListReplaceId() {
        return R.id.match_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.setAction(works.tonny.mobile.demo6.WebActivity.VIEW);
                intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
                intent.putExtra("url",  (String) item.get("url"));
                intent.putExtra("title", getListTitle());
                startActivity(intent);
            }
        };
    }


    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("image", R.id.list_item_image);
        mapping.put("title", R.id.list_item_title);
        mapping.put("date", R.id.match_date);
        mapping.put("time", R.id.match_time);
//        mapping.put("jbdw", R.id.match_jbdw);
//        mapping.put("cbdw", R.id.match_cbdw);
//        mapping.put("cp", R.id.match_cp);
        mapping.put("addr", R.id.match_addr);
        return mapping;
    }


    @Override
    protected int getItemLayout() {
        return R.layout.match_index_item_grid2;
    }


    @Override
    protected boolean loadMore() {
        return true;
    }


    @Override
    protected String getResultDataName() {
        return "data.match.item";
    }
}
