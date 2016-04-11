package works.tonny.mobile.demo6.breed;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.LayoutUtils;
import works.tonny.mobile.demo6.FormActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.widget.DateDialogFragment;


public class XinshqAddActivity extends FormActivity {

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_xinshq_add);
        final ActivityHelper helper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("新生犬申请").setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String pqbh = helper.getValue(R.id.pqbh);
                if (!assertNotNull(pqbh, "请填写配犬编号")) return;

                String xinshq_date = helper.getValue(R.id.xinshq_date);
                if (!assertNotNull(xinshq_date, "请填写幼犬出生日期")) return;

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("action", "xinshengquan");
                map.put("provenum", pqbh);
                map.put("birthdate", xinshq_date);
                map.put("nums", helper.getValue(R.id.nums));
                String csg = helper.getValue(R.id.csqz_g);
                map.put("fnums", csg);
                String csm = helper.getValue(R.id.csqz_m);
                map.put("mnums", csm);
                map.put("fdeath", helper.getValue(R.id.st_g));
                map.put("mdeath", helper.getValue(R.id.st_m));
                map.put("fdeathyou", helper.getValue(R.id.swyq_g));
                map.put("mdeathyou", helper.getValue(R.id.swyq_m));
                String sqg = helper.getValue(R.id.sqqs_g);
                map.put("fregister", sqg);
                String sqm = helper.getValue(R.id.sqqs_m);
                map.put("mregister", sqm);
                if (NumberUtils.toInt(sqg) < NumberUtils.toInt(csg)) {
                    LayoutUtils.alert(XinshqAddActivity.this, "申请犬数不能小于出生犬数").show();
                    return;
                }
                if (NumberUtils.toInt(sqm) < NumberUtils.toInt(csm)) {
                    LayoutUtils.alert(XinshqAddActivity.this, "申请犬数不能小于出生犬数").show();
                    return;
                }
                new Post().execute(map);
            }
        });
        final TextView date = (TextView) findViewById(R.id.xinshq_date);
        final DateDialogFragment fragment = new DateDialogFragment();
        helper.makeItDate(R.id.xinshq_date, calendar);
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_xinshengquanadd);
    }

    @Override
    protected String getActivityTitle() {
        return "新生犬申请";
    }
}
