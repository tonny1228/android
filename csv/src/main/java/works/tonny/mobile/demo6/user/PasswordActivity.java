package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.demo6.LoginActivity;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.AuthException;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.http.HttpRequestException;

public class PasswordActivity extends Activity {

    private ActivityHelper instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_password_activity);
        instance = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).enableBack().setTitle("修改密码");
        instance.addTextChangedListener(R.id.login_password, R.id.ymm);
        instance.addTextChangedListener(R.id.login_newpassword, R.id.xmm);
        instance.addTextChangedListener(R.id.login_confirmpassword,R.id.qrmm);
        instance.setOnClickListener(R.id.button_login, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = instance.getValue(R.id.login_password);
                String n = instance.getValue(R.id.login_newpassword);
                String c = instance.getValue(R.id.login_confirmpassword);
                if (!Application.getUser().getPassword().equals(old)) {
                    Toast.makeText(PasswordActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isEmpty(n)) {
                    Toast.makeText(PasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!c.equals(n)) {
                    Toast.makeText(PasswordActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }


                Map<String, String> map = new HashMap<String, String>();
                map.put("action", "updatepassword");
                map.put("oldpassword", old);
                map.put("newpassword", n);
                new Post().execute(map);
            }
        });

    }


    class Post extends AsyncTask<Map<String, String>, Integer, Boolean> {

        private HttpRequest request;
        private ProgressDialog myDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myDialog = ProgressDialog.show(PasswordActivity.this, "修改密码", "提交中", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            myDialog.dismiss();
            if (result == null) {
                LoginActivity.startLoginActivity(PasswordActivity.this, null);
                return;
            }
            Toast.makeText(PasswordActivity.this, "提交" + (result ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
            if (result) {
                Application.getUser().setPassword(instance.getValue(R.id.login_newpassword));
                Application.saveUser(Application.getUser());
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Map<String, String>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Post, Application.getUrl(R.string.url_user_password));
            Map<String, String> param = params[0];
            for (String key : param.keySet()) {
                request.setFormParam(key, param.get(key));
            }
            String xml = null;
            try {
                xml = request.executeToString();
            } catch (HttpRequestException e) {
                e.printStackTrace();
                return false;
            } catch (AuthException e) {
                e.printStackTrace();
                return null;
            }
            return xml.contains("success");
        }
    }

}
