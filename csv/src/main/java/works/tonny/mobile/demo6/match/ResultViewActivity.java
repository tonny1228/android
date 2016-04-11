package works.tonny.mobile.demo6.match;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.widget.DataView;

public class ResultViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.match_result_view);
            TitleHelper.getInstance(this).enableBack().setTitle("比赛成绩");
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            Map<String, Object> data = (Map<String, Object>) bundle.getSerializable("data");
            Log.info(data);
            ActivityHelper activityHelper = ActivityHelper.getInstance(this);
            activityHelper.setText(R.id.mc, (String) data.get("title"));
            activityHelper.setText(R.id.xtzsh, (String) data.get("blood"));
            activityHelper.setText(R.id.eh, (String) data.get("earid"));
//        activityHelper.setText(R.id.zb, (String) data.get("zb"));
//        activityHelper.setText(R.id.jb, (String) data.get("place"));
//        activityHelper.setText(R.id.mci, (String) data.get("num"));

            Map group = (Map) data.get("group");
            ArrayList<? extends Map<String, ?>> score = (ArrayList<? extends Map<String, ?>>) group.get("score");
            Log.info(score);
            DataView dataView = DataView.newInstance(score, R.layout.match_result_detail);
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("ss", R.id.ss);
            map.put("zb", R.id.zb);
            map.put("place", R.id.place);
            map.put("placenum", R.id.placenum);
            dataView.setMapping(map);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.result, dataView);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.error(e);
        }
    }

}
