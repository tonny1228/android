package works.tonny.mobile.demo6.breed;

import android.os.Bundle;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.FormActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;


public class PeiqzmAddActivity extends FormActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_peiqzm_add);
        final ActivityHelper helper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("申请配犬证明").setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sl = helper.getValue(R.id.sl);
                if (!assertNotNull(sl, "请填写数量")) return;

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("action", "peiquanzhengming");
                map.put("servicename", "申请配犬证明");
                map.put("nums", sl);
                map.put("drawmodes", helper.getValue(R.id.lqfs));
                map.put("remark", helper.getValue(R.id.bz));
                new Post().execute(map);
            }
        });
        helper.setText(R.id.username, "会员ID：" + Application.getUser().getUsername());
        helper.setText(R.id.name,"姓名："+Application.getUser().getName());
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_peiquanzhengming_add);
    }

    @Override
    protected String getActivityTitle() {
        return "申请配犬证明";
    }

}
