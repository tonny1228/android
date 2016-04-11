package works.tonny.mobile.demo6.breed;

import android.os.Bundle;
import android.view.View;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.FormActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;


public class PeiQuanxxAddActivity extends FormActivity {
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_pei_quan_add);
        final ActivityHelper helper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("配犬信息提交").setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pqbh = helper.getValue(R.id.pqbh);
                if (!assertNotNull(pqbh, "请填写配犬编号")) return;

                String pqrq = helper.getValue(R.id.pqrq);
                if (!assertNotNull(pqrq, "请填写配犬日期")) return;

                String gqzsh = helper.getValue(R.id.gqzsh);
                if (!assertNotNull(gqzsh, "请填写公犬血统证书号")) return;

                String gqeh = helper.getValue(R.id.gqeh);
                if (!assertNotNull(gqeh, "请填写公犬耳号")) return;

                String mqzsh = helper.getValue(R.id.mqzsh);
                if (!assertNotNull(mqzsh, "请填写母犬血统证书号")) return;

                String mqeh = helper.getValue(R.id.mqeh);
                if (!assertNotNull(mqeh, "请填写母犬耳号")) return;

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("action", "peiquanxinxi");
                map.put("provenum", pqbh);
                map.put("Datetimes", pqrq);
                map.put("blood1", gqzsh);
                map.put("earid1", gqeh);
                map.put("blood2", mqzsh);
                map.put("earid2", mqeh);

                new Post().execute(map);
            }
        });
        ActivityHelper.getInstance(this).makeItDate(R.id.pqrq, calendar);

    }


    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_peiquanzmadd);
    }

    @Override
    protected String getActivityTitle() {
        return "配犬信息提交";
    }

}
