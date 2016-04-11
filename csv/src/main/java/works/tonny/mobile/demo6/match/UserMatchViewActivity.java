package works.tonny.mobile.demo6.match;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.demo6.query.QzcxViewActivity;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.DataView;
import works.tonny.mobile.widget.RequestActivity;

public class UserMatchViewActivity extends RequestActivity {

    @Override
    protected void create() {
        setContentView(R.layout.match_user_match_view);
        TitleHelper.getInstance(this).enableBack().setTitle("我的参赛");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Map<String, Object> data = (Map<String, Object>) bundle.getSerializable("data");
        Log.info(data);
        ActivityHelper activityHelper = ActivityHelper.getInstance(this);
        activityHelper.setText(R.id.mc, (String) data.get("title"));
        activityHelper.setText(R.id.rq, (String) data.get("date"));
        activityHelper.setText(R.id.je, (String) data.get("je"));
        activityHelper.setText(R.id.sslx, (String) data.get("type"));
        new Request().execute((String) data.get("url"));
    }

    @Override
    protected void executeResult(Map<String, Object> result) {
        final DataView dataView = DataView.newInstance(result.get("data.list.item"), R.layout.match_my_detail_item);
        Map<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put("title", R.id.title);
        mapping.put("blood", R.id.blood);
        mapping.put("earid", R.id.earid);
        mapping.put("zb", R.id.zb);
        dataView.setMapping(mapping);
        dataView.setItemClickListener(new DataView.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", dataView.getAdapter().getData().get(position).get("url").toString());
                startActivity(IntentUtils.newInstance(UserMatchViewActivity.this, QzcxViewActivity.class, bundle));
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.dogs, dataView);
        fragmentTransaction.commit();
    }

}
