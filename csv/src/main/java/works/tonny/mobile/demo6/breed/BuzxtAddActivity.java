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

public class BuzxtAddActivity extends FormActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_buzxt_add);
        final ActivityHelper helper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("补做血统证书申请").setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eh = helper.getValue(R.id.eh);
                if (!assertNotNull(eh, "请填写耳号")) return;

                String xtzsh = helper.getValue(R.id.xtzsh);
                if (!assertNotNull(xtzsh, "请填写血统证书号")) return;

                String bzzsyy = helper.getValue(R.id.bzzsyy);
                if (!assertNotNull(bzzsyy, "请填写证书补做原因")) return;


                Map<String, Object> map = new HashMap<String, Object>();
                map.put("eh", eh);
                map.put("xtzsh", xtzsh);
                map.put("bzzsyy", bzzsyy);
                new Post().execute(map);
            }
        });
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_buzxtzs_add);
    }

    @Override
    protected String getActivityTitle() {
        return "配犬信息提交";
    }


}
