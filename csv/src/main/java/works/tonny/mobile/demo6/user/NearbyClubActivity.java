package works.tonny.mobile.demo6.user;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;

public class NearbyClubActivity extends ListActivity {
    private String url;

    @Override
    protected String getListTitle() {
        return "附近的俱乐部";
    }

    @Override
    protected int getItemLayout() {
        return R.layout.user_nearbyclub_item;
    }

    @Override
    protected String getUrl() {
        if (url == null) {
            url = Application.getUrl(R.string.url_location_club).replace("${latitude}", getIntent().getStringExtra("latitude")).replace("${longitude}", getIntent().getStringExtra("longitude"));
        } return url;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                item.put("cur_latitude", getIntent().getStringExtra("latitude"));
                item.put("cur_longitude", getIntent().getStringExtra("longitude"));
                Intent intent = IntentUtils.newInstance(NearbyClubActivity.this, BaiduMapActivity.class, item);
                startActivity(intent);
            }
        };
    }


    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>(); mapping.put("name", R.id.name);
        mapping.put("distance", R.id.distance); mapping.put("linkman", R.id.linkman); mapping.put("phone", R.id.phone);
        mapping.put("img", R.id.user_head); return mapping;
    }
}
