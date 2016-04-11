package works.tonny.mobile.autobackup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import works.tonny.mobile.DeviceUtils;
import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2016/2/23.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    public static List<NetEventHandler> mListeners = new ArrayList<NetEventHandler>();
    private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.info("网络变化了");
        if (!DeviceUtils.isWifiConnected(context)) {
            if (BackupService.getInstance() != null && BackupService.getInstance().getState() == 1) {
                Intent service = new Intent(context, BackupService.class);
                context.stopService(service);
                BackupService.getInstance().setState(0);
        }
            return;
        }
        if (BackupService.getInstance() != null && BackupService.getInstance().getState() == 1) {
            return;
        }
        Intent service = new Intent(context, BackupService.class);
        context.startService(service);
    }

    public static abstract interface NetEventHandler {

        public abstract void onNetChange();
    }
}