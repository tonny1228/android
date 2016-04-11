package works.tonny.mobile.demo6;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.util.Map;

import works.tonny.mobile.Application;
import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.Launcher;
import works.tonny.mobile.http.AbstractHttpRequest;
import works.tonny.mobile.http.HttpRequest;
import works.tonny.mobile.service.UpdateService;
import works.tonny.mobile.utils.Log;
import works.tonny.mobile.utils.XMLParser;
import works.tonny.mobile.widget.IDLinkedHashMap;


public class WelcomeActivity extends Activity {
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Launcher.init(this.getBaseContext());
        setContentView(R.layout.activity_welcome);
        ImageView image = (ImageView) findViewById(R.id.welcomeimageView);
        final Runnable task = new Runnable() {
            public void run() {

            }
        };


        if (DeviceUtils.isNetworkConnected(this)) {
            new CheckUpdate().execute();
        }

        handler.postDelayed(task, 1000);//延迟调用

        Application.setHost(R.string.host);
    }

    void goon() {
        Intent intent = new Intent();
        intent.setClass(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    class CheckUpdate extends AsyncTask<Map<String, String>, Integer, IDLinkedHashMap> {
        private HttpRequest request;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            if (result == null) {
                return;
            }

            Object version = result.get("version");
            final Object isForced = result.get("isForced");
            Log.info("当前版本：" + Application.getVersionName() + ",服务器版本：" + version);
            if (Application.getVersion().compareTo(version.toString()) < 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);

                builder.setTitle("新版本");
                builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(WelcomeActivity.this, UpdateService.class);
                        intent.putExtra("Key_App_Name", "应用");
                        intent.putExtra("Key_Down_Url", result.get("url").toString());
                        intent.putExtra("icon", R.drawable.ic_launcher);
                        startService(intent);
                        dialog.dismiss();
                        if (!"1".equals(isForced)) {
                            goon();
                        }
                    }
                });
                if (!"1".equals(isForced)) {
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            goon();
                        }
                    });
                    builder.setMessage("发现版本" + version + ",现在下载升级吗？");
                } else {
                    builder.setMessage("发现版本" + version + ",请下载升级之后再访问");
                    builder.setCancelable(false);
                }
                builder.create().show();
            } else {
                goon();
            }
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
