package works.tonny.mobile.autobackup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import works.tonny.mobile.FileUtils;
import works.tonny.mobile.utils.IOUtils;
import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2016/2/23.
 */
public class FileService {

    private static final String FILENAME = "/cache";
    private static List<BackupConfig> backupList;

    public static List<BackupConfig> list() {
        File cacheDirFile = null;
        try {
            cacheDirFile = FileUtils.getCacheDirFile(FILENAME);
            backupList = (List<BackupConfig>) IOUtils.getCachedObject(cacheDirFile);
        } catch (IOException e) {
            Log.error(e);
            if (cacheDirFile.exists()) {
                cacheDirFile.delete();
            }
        }
        if (backupList == null) {

            backupList = new ArrayList<BackupConfig>();
        }

        if (backupList.isEmpty()) {
//            BackupConfig backup = new BackupConfig("192.168.0.128", "movie", "tonny1230@hotmail.com", "oiamlxd");
//            backup.getLocals().add(new File("/storage/emulated/0/baidu"));
//            backupList.add(backup);
//            save();
        }
        return backupList;
    }


    public static void save() {
        try {
            IOUtils.cacheObject(backupList, FileUtils.getCacheDirFile(FILENAME));
        } catch (IOException e) {
            Log.error(e);
        }
    }

}
