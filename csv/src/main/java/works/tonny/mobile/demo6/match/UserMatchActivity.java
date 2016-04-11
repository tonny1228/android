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

public class UserMatchActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return "我的参赛";
    }

    @Override
    protected int getItemLayout() {
        return R.layout.match_user_list_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_match_user);
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
        mapping.put("title", R.id.mc);
        mapping.put("date", R.id.rq);
        mapping.put("je", R.id.je);
        mapping.put("type", R.id.sslx);
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
                intent.setClass(view.getContext(), UserMatchViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) item);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }

}
