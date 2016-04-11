package works.tonny.mobile.demo6.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

import works.tonny.mobile.ActivityHelper;
import works.tonny.mobile.Application;
import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.demo6.R;
import works.tonny.mobile.demo6.TitleHelper;
import works.tonny.mobile.demo6.WebActivity;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.service.UpdateService;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.IDLinkedHashMap;
import works.tonny.mobile.widget.LoadingDialog;

public class SettingActivity extends Activity {
    private ActivityHelper instance;
    private CheckUpdate checkUpdate;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_activity);
        instance = ActivityHelper.getInstance(this);
        TitleHelper.getInstance(this).setTitle("设置").enableBack();
        instance.setOnClickListener(R.id.password, PasswordActivity.class);

        instance.setOnClickListener(R.id.update, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUpdate = new CheckUpdate();
                checkUpdate.execute();
            }
        });

        new Request().execute(Application.getUrl(R.string.url_setting));
    }


    class Request extends AsyncTask<String, Integer, Map<String, Object>> {


        @Override
        protected void onPostExecute(final Map<String, Object> data) {
            loadingDialog.dismiss();
            instance.setOnClickListener(R.id.about, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(WebActivity.VIEW);
                    intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
                    Log.info(getIntent().getStringExtra("url"));
                    intent.putExtra("title", "关于");
                    intent.putExtra("url", (String) ((Map) data.get("data.item[1]")).get("url"));
//                    intent.putExtra("url", "http://192.168.0.18/about.html");
                    startActivity(intent);
                }
            });
            instance.setOnClickListener(R.id.xieyi, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(WebActivity.VIEW);
                    intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
                    Log.info(getIntent().getStringExtra("url"));
                    intent.putExtra("title", "用户协议");
                    intent.putExtra("url", (String) ((Map) data.get("data.item[2]")).get("url"));
                    startActivity(intent);
                }
            });
            instance.setOnClickListener(R.id.ystk, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(WebActivity.VIEW);
                    intent.addCategory(android.content.Intent.CATEGORY_DEFAULT);
                    Log.info(getIntent().getStringExtra("url"));
                    intent.putExtra("title", "隐私条款");
                    intent.putExtra("url", (String) ((Map) data.get("data.item[3]")).get("url"));
                    startActivity(intent);
                }
            });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = LoadingDialog.newInstance(SettingActivity.this);
            loadingDialog.show();
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            if (!DeviceUtils.isNetworkConnected(SettingActivity.this)) {
                this.cancel(true);
            }
            HttpRequest request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, params[0]);
            try {
                XMLParser xmlParser = new XMLParser();
                String xml = request.executeToString();
                Log.info(xml);
                xmlParser.parse(xml);
                IOUtils.cacheObject(xmlParser.getDatas(), FileUtils.getCacheDirFile("/user"));
                return (Map<String, Object>) xmlParser.getDatas();
            } catch (Exception e) {
                e.printStackTrace();
            }
            request = null;
            return null;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (checkUpdate != null)
            checkUpdate.cancel(true);
    }


    class CheckUpdate extends AsyncTask<Map<String, String>, Integer, IDLinkedHashMap> {
        private HttpRequest request;
        private View progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = findViewById(R.id.update_progress);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            request.cancel();
            Log.info("取消了");
        }


        @Override
        protected void onPostExecute(final IDLinkedHashMap result) {
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);

            if (result == null) {
                Toast.makeText(SettingActivity.this, "没有更新", Toast.LENGTH_SHORT).show();
                return;
            }

            Object version = result.get("version");
            Log.info("当前版本：" + Application.getVersionName() + ",服务器版本：" + version);
            if (!Application.getVersionName().equals(version) && !this.isCancelled()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("发现版本" + version + ",要升级吗？");
                builder.setTitle("新版本");
                builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SettingActivity.this, UpdateService.class);
                        intent.putExtra("Key_App_Name", "应用");
                        intent.putExtra("Key_Down_Url", result.get("url").toString());
                        intent.putExtra("icon", R.drawable.ic_launcher);
                        startService(intent);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            } else {
                Toast.makeText(getApplicationContext(),"已经是最新版本",Toast.LENGTH_SHORT).show();
            }
            checkUpdate = null;
        }

        @Override
        protected IDLinkedHashMap doInBackground(Map<String, String>... params) {
            request = AbstractHttpRequest.getInstance(HttpRequest.Method.Get, Application.getUrl(R.string.url_setting_update));
            String xml = null;
            try {
                xml = request.executeToString();
                XMLParser xmlParser = new XMLParser();
                xmlParser.parse(xml);
                return (IDLinkedHashMap) xmlParser.getDatas().get("data");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
