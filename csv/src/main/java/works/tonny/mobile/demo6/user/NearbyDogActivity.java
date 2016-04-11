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
import works.tonny.mobile.demo6.query.QzcxViewActivity;

public class NearbyDogActivity extends ListActivity {

    @Override
    protected String getListTitle() {
        return "附近的种犬";
    }

    @Override
    protected int getItemLayout() {
        return R.layout.user_nearbydog_item;
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_location_dog).replace("${latitude}", getIntent().getStringExtra("latitude"))// )
                .replace("${longitude}", getIntent().getStringExtra("longitude"));//);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
                item.put("cur_latitude", getIntent().getStringExtra("latitude"));
                item.put("cur_longitude", getIntent().getStringExtra("longitude"));
                Intent intent = IntentUtils.newInstance(NearbyDogActivity.this.getBaseContext(), QzcxViewActivity.class, item);
                startActivity(intent);
            }
        };
    }

    @Override
    protected Map<String, Integer> getMapping() {
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("cname", R.id.cname);
        mapping.put("ename", R.id.ename);
        mapping.put("distance", R.id.distance);
        mapping.put("linkman", R.id.linkman);
        mapping.put("phone", R.id.phone);
        mapping.put("img", R.id.user_head);
        return mapping;
    }

}
