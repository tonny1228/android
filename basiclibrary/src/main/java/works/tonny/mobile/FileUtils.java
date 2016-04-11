package works.tonny.mobile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by tonny on 2015/6/24.
 */
public class FileUtils {

    public static final String WEB_CACHE_DIR = "/tonny/webcache/";

    private static File externalCacheDir;
    private static String packageName;
    static File cacheDir;

    static void init(Context context) {
        externalCacheDir = context.getExternalCacheDir();
        packageName = context.getPackageName();
        Log.i("tonny", "Environment.getDataDirectory()::::::::::::::::::::::::::::::" + Environment.getDataDirectory());
        Log.i("tonny", "Environment.getExternalStorageDirectory()::::::::::::::::::::::::::::::" + Environment.getExternalStorageDirectory());
        Log.i("tonny", "context.getExternalCacheDir()::::::::::::::::::::::::::::::" + externalCacheDir);
        cacheDir = context.getCacheDir();
        Log.i("tonny", "context.getCacheDir()::::::::::::::::::::::::::::::" + cacheDir);
        Log.i("tonny", "context.getFilesDir()::::::::::::::::::::::::::::::" + context.getFilesDir());

    }


    public static File getExternalStorageDirectory(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getParent(), "external1");
            if (file.exists()) {
                return new File(file, path);
            }
            return new File(Environment.getExternalStorageDirectory().getPath() + path);
        } else {
            return getExternalCacheDir(path);
        }
    }

    /**
     * 获取外部缓存目录
     *
     * @param path
     * @return
     */
    public static File getExternalCacheDir(String path) {
        if (externalCacheDir == null && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            externalCacheDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + packageName + "/cache/" + path);
            externalCacheDir.getParentFile().mkdirs();
        }
        return externalCacheDir;
    }

    public static File getCacheDirFile(String path) {
//        File f = new File(cacheDir.getPath() + "/Android/data/" + packageName + "/cache/" + path);
        File f = new File(cacheDir.getPath() + path);
        File parentFile = f.getParentFile();
        if (!parentFile.exists())
            parentFile.mkdirs();
        return f;
    }


    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static void save(String uri, File file) {

    }
}
