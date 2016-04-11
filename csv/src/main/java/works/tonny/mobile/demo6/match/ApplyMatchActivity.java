package works.tonny.mobile.demo6.match;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;

public class ApplyMatchActivity extends ListActivity {
    @Override
    protected String getListTitle() {
        return "申请参赛";
    }

    @Override
    protected boolean isAddEnabled() {
        return false;
    }

    @Override
    protected Class getAddActivityClass() {
        return null;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.matchapply_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_match_apply_list);
    }


    @Override
    protected int getButtonIcon() {
        return 0;
    }

    @Override
    protected boolean loadMore() {
        return false;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ApplyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) item);
                intent.putExtras(bundle);
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
        mapping.put("addr", R.id.match_addr);
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
    protected String getResultDataName() {
        return "data.match.item";
    }
}
