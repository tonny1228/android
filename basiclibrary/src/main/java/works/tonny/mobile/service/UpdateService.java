package works.tonny.mobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import works.tonny.mobile.FileUtils;
import works.tonny.mobile.R;
import works.tonny.mobile.utils.Log;


/**
 * 升级服务
 *
 * @author hexiaoming
 */
public class UpdateService extends Service {

    public static final String INSTALL_APK = "Install_Apk";
    /**
     * *****download progress step********
     */
    private static final int down_step_custom = 3;

    private static final int TIMEOUT = 20 * 1000;// 超时
    private static String down_url;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;

    private String app_name;

    private NotificationManager notificationManager;
    private Notification notification;
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private RemoteViews contentView;
    private File file;
    private int icon;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * 方法描述：onStartCommand方法
     *
     * @param intent, flags, startId
     * @return int
     * @see UpdateService
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        app_name = intent.getStringExtra("Key_App_Name");
        down_url = intent.getStringExtra("Key_Down_Url");
        icon = intent.getIntExtra("icon", R.drawable.notification_template_icon_bg);

        // create file,应该在这个地方加一个返回值的判断SD卡是否准备好，文件是否创建成功，等等！
        Log.info(app_name + down_url);
        createNotification();
        createThread(down_url);
        return super.onStartCommand(intent, flags, startId);
    }


    private void installApk() {
        // TODO Auto-generated method stub
        /*********下载完成，点击安装***********/
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        /**********加这个属性是因为使用Context的startActivity方法的话，就需要开启一个新的task**********/
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        UpdateService.this.startActivity(intent);
    }

    /**
     * 方法描述：createThread方法, 开线程下载
     *
     * @param
     * @return
     * @see UpdateService
     */
    public void createThread(String url) {
        new Download().execute(url);
    }


    public class Download extends AsyncTask<String, Integer, File> {
        @Override
        protected File doInBackground(String... params) {
            int down_step = down_step_custom;// 提示step
            int totalSize;// 文件总大小
            int downloadCount = 0;// 已经下载好的大小
            int updateCount = 0;// 已经上传的文件大小
            file = FileUtils.getExternalStorageDirectory("/download/tonny.apk");

            try {
                InputStream inputStream;
                OutputStream outputStream;
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(TIMEOUT);
                httpURLConnection.setReadTimeout(TIMEOUT);
                // 获取下载文件的size
                totalSize = httpURLConnection.getContentLength();

                if (httpURLConnection.getResponseCode() == 404) {
                    throw new Exception("fail!");
                }

                inputStream = httpURLConnection.getInputStream();
                outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉

                byte buffer[] = new byte[1024];
                int readsize = 0;
                while ((readsize = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readsize);
                    downloadCount += readsize;// 时时获取下载到的大小
                    if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                        updateCount += down_step;
                        publishProgress(updateCount);
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return file;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // 改变通知栏
            contentView.setTextViewText(R.id.notificationPercent, values[0] + "%");
//            contentView.setProgressBar(R.id.notificationProgress, 100, values[0], false);
//            notification.contentView = contentView;
            notificationManager.notify(R.layout.notification_item, notification);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                /*********下载完成，点击安装***********/
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);

                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.setLatestEventInfo(UpdateService.this, app_name, "下载完成", pendingIntent);
                notificationManager.notify(R.layout.notification_item, notification);

                /*****安装APK******/
//            installApk();
                //stopService(updateIntent);
                /***stop service*****/
                stopSelf();
            } else {
                Log.info("下载失败");
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                //notification.setLatestEventInfo(UpdateService.this,app_name,"下载失败", pendingIntent);
                notification.setLatestEventInfo(UpdateService.this, app_name, "下载失败", null);
                notificationManager.notify(R.layout.notification_item, notification);
                /***stop service*****/
                //onDestroy();
                stopSelf();
            }
        }
    }


    /**
     * 方法描述：createNotification方法
     *
     * @param
     * @return
     * @see UpdateService
     */
    public void createNotification() {

        Log.info("create ting tongzhi");
        //notification = new Notification(R.drawable.dot_enable,app_name + getString(R.string.is_downing) ,System.currentTimeMillis());
        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, app_name + "下载中");
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

        Notification.Builder builder = new Notification.Builder(getBaseContext());
        builder.setSmallIcon(icon).setContent(contentView);


//        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //notification.flags = Notification.FLAG_AUTO_CANCEL;


//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(R.layout.notification_item, notification);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = builder.getNotification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(R.layout.notification_item, notification);
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}