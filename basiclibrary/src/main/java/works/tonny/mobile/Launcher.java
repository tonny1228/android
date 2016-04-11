package works.tonny.mobile;

import android.content.Context;

import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2015/7/4.
 */
public class Launcher {
    public static boolean launchered;

    public static Context context;

    public static void init(Context context) {
        if (FileUtils.cacheDir == null) {
            Application.init(context);
            LayoutUtils.init(context);
            FileUtils.init(context);
            DeviceUtils.init(context);
            Log.init(context);
        }
        launchered = true;
    }

    public static boolean isLaunchered() {
        return launchered;
    }
}
