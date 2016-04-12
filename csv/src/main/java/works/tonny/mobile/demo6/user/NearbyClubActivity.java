package works.tonny.mobile.demo6.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.ListActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.widget.AbstractListActivity;

public class NearbyClubActivity extends AbstractListActivity {
    private String url;

    private TitleHelper titleHelper;

    public NearbyClubActivity() {
        setItemLayout(R.layout.user_nearbyclub_item);
        addMapping("name", R.id.name);
        addMapping("distance", R.id.distance);
        addMapping("linkman", R.id.linkman);
        addMapping("phone", R.id.phone);
        addMapping("img", R.id.user_head);
        setOnLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String[] phones = listFragment.getmAdapter().getData().get(position).get("phone").toString().split("[^\\d]+");
                for (int i = 0; i < phones.length; i++) {
                    phones[i] = "拨打" + phones[i];
                }
                new AlertDialog.Builder(NearbyClubActivity.this).setItems(phones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phones[which].substring(2)));
                        startActivity(intent);
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_list;
    }


    @Override
    protected void beforeCreate() {
        setUrl(Application.getUrl(R.string.url_location_club).replace("${latitude}", getIntent().getStringExtra("latitude")).replace("${longitude}", getIntent().getStringExtra("longitude")));
    }

    @Override
    protected void init() {
        titleHelper = TitleHelper.getInstance(this);
        titleHelper.enableBack().setTitle("附近的俱乐部");
    }

    @Override
    protected int getListReplaceId() {
        return R.id.list;
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


}
