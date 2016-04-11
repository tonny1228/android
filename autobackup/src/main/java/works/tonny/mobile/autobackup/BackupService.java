package works.tonny.mobile.autobackup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.FileUtils;
import works.tonny.mobile.Launcher;
import works.tonny.mobile.utils.DateUtils;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2016/1/20.
 */
public class BackupService extends Service {
    private int total;
    private int finished;
    private Backup task;
    private int state;
    private static BackupService instance;
    private String current;
    private NotificationManager notificationManager;
    private Notification notification;
    private Listener listener;
    private int failed = 0;


    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public boolean isFinished() {
        return finished == total;
    }

    public String getCurrent() {
        return current;
    }

    public int getState() {
        return state;
    }

    public static BackupService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.info("create");
        instance = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!Launcher.isLaunchered()) {
            Launcher.init(getBaseContext());
        }

        total = 0;
        finished = 0;
        failed = 0;
        createNotification("准备备份", "初始化", false, 0, 0);

        if (!DeviceUtils.isWifiConnected(getBaseContext())) {
//            notice(true, "WIFI没有连接", 0, 0);
            createNotification("备份失败", "WIFI没有连接", false, 0, 0);
            Log.info("nowifi");
            return super.onStartCommand(intent, flags, startId);
        }
        Log.info("wifi");

//        notice(false, "扫描中", 0, 0);
        List<BackupConfig> backupList = FileService.list();

        state = 1;
        task = new Backup();
        task.execute(backupList);
//        this.button = (Button) intent.getSerializableExtra("view");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.info("destroy");
        finish();
//        contentView.setImageViewResource(R.id.notificationImage, R.drawable.icon_back);
//        notificationManager.notify(R.layout.notification_item, notification);
//        button.setText("备份");
    }


    private void finish() {
        state = 0;
        if (task == null) {
            return;
        }
        task.cancel(true);
        FileService.save();
        createNotification("备份结束", "计划" + total + "个，完成" + finished + "个" + "," + failed + "个失败", false, 0, 0);
    }


    /**
     * 方法描述：createNotification方法
     *
     * @param
     * @return
     */
    public void createNotification(String title, String content, boolean canCancel, int max, int current) {
//        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
//        contentView.setTextViewText(R.id.notificationTitle, "备份器");
//        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
//
//
//        Notification.Builder builder = new Notification.Builder(getBaseContext());
//        builder.setSmallIcon(R.drawable.logo).setContent(contentView);
//
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notification = builder.getNotification();
//        notification.flags = Notification.FLAG_ONGOING_EVENT;
//        notificationManager.notify(R.layout.notification_item, notification);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        int flags = Notification.FLAG_AUTO_CANCEL;
        if (canCancel) {
            flags = Notification.FLAG_AUTO_CANCEL;
        }
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content)
                .setContentIntent(getDefalutIntent(flags)) //设置通知栏点击意图
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setProgress(max, current, false)
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        notification = mBuilder.build();
        mNotificationManager.notify(1, notification);
        if (listener != null)
            listener.onMessage(state, max, current, StringUtils.defaultString(this.current));
    }

    public PendingIntent getDefalutIntent(int flags) {
        Intent intent = new Intent(this, ServiceListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, flags);
        return pendingIntent;
    }

    class Backup extends AsyncTask<List<BackupConfig>, String, Integer> {

        int finish = 0;

        @Override
        protected void onPreExecute() {
            createNotification("准备备份", "准备中", false, 0, 0);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            finished += 1;
//            ServiceListActivity.showMessage(finished + "/" + total + "\n/" + values[0].toString());
            current = values[0].toString();
//            notice(false, "备份中，" + finished + "/" + total, total, finished);
            createNotification("备份中", finished + "/" + total + "," + failed + "个失败", false, total, finished);

        }

        @Override
        protected void onPostExecute(Integer integer) {
//            Toast.makeText(BackupService.this, "完成", Toast.LENGTH_SHORT).show();
            finish();
            if (integer == 1) {
                createNotification("备份终止", "WIFI中断，已完成" + finished + "/" + total + "," + failed + "个失败", false, total, finished);
            }
            stopSelf();
//            notice(true, "备份结束", total, finished);
            Log.info("backendddddddddddddddddddddddd");

        }

        /**
         * 备份通讯录
         *
         * @return
         */
        private boolean backupContact(BackupConfig backup) {
            boolean success = false;
            SmbFileOutputStream sout = null;
            FileInputStream in = null;
            for (int i = 0; i < 3; i++) {
                try {
                    SmbFile contact = new SmbFile(backup.toString() + DateUtils.toString("yyyy-MM-dd") + "导出的联系人.vcf");
                    if (!contact.exists()) {
                        sout = new SmbFileOutputStream(contact);
//                Log.info(file1.toString());
                        in = new FileInputStream(FileUtils.getCacheDirFile("/contact"));
                        IOUtils.copy(in, sout);
                        success = true;
                    } else {
                        success = true;
                    }
                    break;
                } catch (IOException e) {
                    Log.error(e);
                } finally {
                    IOUtils.close(in);
                    IOUtils.close(sout);
                }
            }
            return success;
        }

        @Override
        protected Integer doInBackground(List<BackupConfig>... params) {
            List<BackupConfig> backupList = params[0];
            ContactReader.read(BackupService.this);


            Log.info("完成通讯录扫描");
            for (BackupConfig backup : backupList) {
                backup.scan();
                Map<File, List<File>> todo = backup.getTodo();
                for (List<File> files : todo.values()) {
                    total += files.size();
                }
            }
            Log.info("完成文件夹扫描");
            for (BackupConfig backup : backupList) {
                if (!backupContact(backup)) { //备份通讯录
                    return 1;
                }
                Log.info("完成通讯录备份" + backup.getHost());
                int serverError = 0;
                for (File f : backup.getLocals()) {
                    List<File> todo = backup.getTodo().get(f);
                    if (todo == null) {
                        continue;
                    }
                    Log.info("备份" + f);
                    while (!todo.isEmpty() && state == 1) {
                        if (!DeviceUtils.isWifiConnected(BackupService.this) || state == 0) {
                            Log.info("取消备份，wifi=" + DeviceUtils.isWifiConnected(BackupService.this) + ";state=" + state);
                            return 1;
                        }
                        File file = todo.get(0);
                        if (serverError > 3) {
                            //同一服务器错误3次继续下一服务器
                            Log.info("本服务器错误3次");
                            break;
                        }


                        try {
                            backupFile(f, file, backup);
                            serverError = 0;
                        } catch (Exception e) {
                            Log.error(e);
                            backup.fail(file);
                            serverError++;
                            failed++;
                        }
                    }
                }
            }
            return 0;
        }


        private boolean backupFile(File base, File file, BackupConfig backup) throws Exception {
            List<File> todo = backup.getTodo().get(base);
            SmbFileOutputStream sout = null;
            InputStream in = null;
            try {
                Log.info(file.toString());
                String folder = file.getParent().replace(base.toString(), "");
                publishProgress(folder + file.getName());

                SmbFile parent = new SmbFile(backup.toString() + base.getName() + "/" + folder);
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                if (!StringUtils.isEmpty(folder)) {
                    folder = folder.substring(1) + "/";
                }
                Log.info(backup.toString() + base.getName() + "/" + folder + file.getName());
                SmbFile smbFile = new SmbFile(backup.toString() + base.getName() + "/" + folder + file.getName());
                if (smbFile.exists() && !backup.getFailed().contains(file)) {
                    backup.finish(file);
                    finish++;
                    todo.remove(0);
                    return false;
                }

                try {
                    sout = new SmbFileOutputStream(smbFile);
                    Log.info(smbFile.toString());
                    in = new FileInputStream(file);
                    IOUtils.copy(in, sout);
                    backup.finish(file);
                    finish++;
                    todo.remove(0);
                } catch (IOException e) {
                    throw e;
                }
//            } catch (Exception e) {
//                Log.error(e);
//                backup.fail(file);
//                serverError++;
//                failed++;
            } finally {
                IOUtils.close(in);
                IOUtils.close(sout);
            }
            return true;
        }

    }


    public static interface Listener {
        void onMessage(int state, int total, int finished, String file);
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public class MsgBinder extends Binder {
        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public BackupService getService() {
            return BackupService.this;
        }
    }


    public void setState(int state) {
        this.state = state;
    }
}
