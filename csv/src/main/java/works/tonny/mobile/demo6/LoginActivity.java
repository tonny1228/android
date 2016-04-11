package works.tonny.mobile.demo6;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.IntentUtils;
import works.tonny.mobile.demo6.user.RegisterActivity;
import works.tonny.mobile.widget.TipWatcher;


public class LoginActivity extends Activity {
    public static final int RESULT_LOGINED = 1228;

    private TextView username;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TitleHelper.getInstance(this).setTitle("登录").enableBack().setButton("注册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = IntentUtils.newInstance(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        ActivityHelper.getInstance(this).setOnClickListener(R.id.goback, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.home();
                finish();
            }
        });

        username = (TextView) findViewById(R.id.login_username);
        final TextView tip = (TextView) findViewById(R.id.username_tip);
        username.addTextChangedListener(new TipWatcher(tip));

        password = (TextView) findViewById(R.id.login_password);
        Button loginButton = (Button) findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = LoginActivity.this.username.getText().toString();
                String password = LoginActivity.this.password.getText().toString();
                Application.login(username, password);
                if (LoginActivity.this.getIntent().getStringExtra("goon") == null) {
                    finish();
                    return;
                }
                Intent intent = new Intent();
                try {
                    intent.setClass(LoginActivity.this, Class.forName(LoginActivity.this.getIntent().getStringExtra("goon")));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                intent.putExtras(LoginActivity.this.getIntent().getExtras());
                startActivity(intent);
                finish();
//                    Toast.makeText(getBaseContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void startLoginActivity(Activity activity, Class result) {
        Intent intent = new Intent();
//        intent.putExtra("goon", "");
        intent.setClass(activity, LoginActivity.class);
        activity.startActivity(intent);

    }

    public static void startLoginActivity(Fragment activity, Class result) {
        Intent intent = new Intent();
//        intent.putExtra("goon", "");
        activity.startActivityForResult(intent, RESULT_LOGINED);

    }

}
