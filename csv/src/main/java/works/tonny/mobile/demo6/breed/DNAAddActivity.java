package works.tonny.mobile.demo6.breed;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.LayoutUtils;
import works.tonny.mobile.demo6.FormActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;


public class DNAAddActivity extends FormActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breed_dna_add);
        final ActivityHelper helper = ActivityHelper.getInstance(this);

        final CheckBox f = (CheckBox) findViewById(R.id.qzjdf);
        final CheckBox m = (CheckBox) findViewById(R.id.qzjdm);

        TitleHelper.getInstance(this).enableBack().setTitle("DNA检测申请").setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xtzsh = helper.getValue(R.id.xtzsh);
                if (!assertNotNull(xtzsh, "请填写血统证书号")) return;


                String eh = helper.getValue(R.id.eh);
                if (!assertNotNull(eh, "请填写耳号")) return;

//                if (!assertTrue(f.isChecked() || m.isChecked(), "请选择亲子鉴定比对方")) return;


                String jy = helper.getValue(R.id.jy);
                String qzjdf = helper.getValue(R.id.qzjdf);
                String qzjdm = helper.getValue(R.id.qzjdm);

                if(StringUtils.isEmpty(jy) &&StringUtils.isEmpty(qzjdf) && StringUtils.isEmpty(qzjdm)){
                    LayoutUtils.alert(DNAAddActivity.this, "请选择检测内容!").show();
                    return;
                }


                Map<String, Object> map = new HashMap<String, Object>();
                map.put("action","dna");
                map.put("blood", xtzsh);
                map.put("earid", eh);
                map.put("ext", jy);
                map.put("ext1", qzjdf);
                map.put("ext2", qzjdm);

                new Post().execute(map);
            }
        });
    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_breed_dnaadd);
    }

    @Override
    protected String getActivityTitle() {
        return "DNA检测申请";
    }

}
