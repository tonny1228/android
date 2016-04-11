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

public class ResultActivity extends ListActivity {
    @Override
    protected String getListTitle() {
        return "比赛成绩";
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
        return R.layout.matchresult_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_match_result);
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
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("title", R.id.list_item_title);
        mapping.put("blood", R.id.match_date);
        mapping.put("earid", R.id.match_time);
//        mapping.put("zb", R.id.zb);
//        mapping.put("place", R.id.jb);
//        mapping.put("num", R.id.mci);
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
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ResultViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }
}
