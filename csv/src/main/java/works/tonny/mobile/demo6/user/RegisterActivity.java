package works.tonny.mobile.demo6.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.MD5;
import works.tonny.mobile.widget.RequestActivity;

public class RegisterActivity extends RequestActivity {

    private ActivityHelper activityHelper;

    private String code;

    private int count = 120;
    private Handler handler;
    private Runnable runnable;
    private Button button;

    @Override
    protected void create() {
        setContentView(R.layout.user_activity_register);
        TitleHelper.getInstance(this).setTitle("注册").enableBack();

        activityHelper = ActivityHelper.getInstance(this);

        activityHelper.setOnClickListener(R.id.button_login, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String value = activityHelper.getValue(R.id.code);
                String mobile = activityHelper.getValue(R.id.login_username);
                String md5 = MD5.encode(value);
                Log.info(md5 + ":" + code);
                if (!md5.equalsIgnoreCase(code)) {
                    Toast.makeText(RegisterActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("mobile", mobile);
                Intent intent = IntentUtils.newInstance(RegisterActivity.this, Login2Activity.class, param);
                RegisterActivity.this.startActivity(intent);
                finish();
            }
        });
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // handler自带方法实现定时器
                try {
                    daojishi();
//                    handler.postDelayed(this, 1000);
                } catch (Exception e) {
                    Log.error(e);
                }
            }
        };
        button = (Button) findViewById(R.id.user_reg_getcode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = activityHelper.getValue(R.id.login_username);
                if (!value.matches("\\d{11}")) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                button.setClickable(false);
                new Request().execute(Application.getUrl(R.string.url_user_getcode) + "?action=sendMobile&mobile=" + value);
                handler.postDelayed(runnable, 100);
                button.setTextColor(Color.parseColor("#888888"));
            }
        });

    }

    public void daojishi() {
        if (count > 0) {
            handler.postDelayed(runnable, 1000);
            button.setText("获取验证码(" + (count--) + ")");

        } else {
            count = 120;
            button.setText("获取验证码");
            button.setClickable(true);
            button.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    protected void executeResult(Map<String, Object> result) {
        //activityHelper.setText(R.id.code, "1234");
        Map tag = (Map) result.get("s2m.body.tag");
        Map list = (Map) tag.get("list");
        Map item = (Map) list.get("item");
        String id = (String) item.get("id");
        code = id;
        Log.info(code);
    }

}
