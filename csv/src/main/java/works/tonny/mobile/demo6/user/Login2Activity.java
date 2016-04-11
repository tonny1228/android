package works.tonny.mobile.demo6.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.FormActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;

public class Login2Activity  extends FormActivity {

    private ActivityHelper activityHelper;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_login2);
        phone = getIntent().getStringExtra("mobile");
        activityHelper = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).setTitle("注册").enableBack().setButton("提交", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pawd = activityHelper.getValue(R.id.login_password);
                String repawd = activityHelper.getValue(R.id.re_password);
                String id = activityHelper.getValue(R.id.id);
                String name = activityHelper.getValue(R.id.name);
                String gender = activityHelper.getValue(R.id.gender);
                String address = activityHelper.getValue(R.id.address);
                if(!repawd.equals(pawd)){
                    Toast.makeText(Login2Activity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!id.matches("(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)")){
                    Toast.makeText(Login2Activity.this,"身份证号不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("action", "register");
                param.put("card", id);
                param.put("approvePhone", phone);
                param.put("password", pawd);
                param.put("firstname", name);
                param.put("sex", gender);
                param.put("address", address);

                new Post().execute(param);
            }
        });

    }

    @Override
    protected String getUrl() {
        return Application.getUrl(R.string.url_user_register);
    }

    @Override
    protected String getActivityTitle() {
        return "注册";
    }
}
