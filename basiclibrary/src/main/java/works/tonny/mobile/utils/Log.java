package works.tonny.mobile.utils;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import works.tonny.mobile.FileUtils;

/**
 * Created by tonny on 2015/7/11.
 */
public class Log {
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");// 日志的输出格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy_MM_dd_");// 日志文件格式

    private static String TAG = "works.tonny";
    private static String pack;

    public static void debug(String message) {
        android.util.Log.d(getClassName(), String.valueOf(message));
        writeLogtoFile("debug", getClassName(), message);
    }

    public static void debug(Number message) {
        String msg = String.valueOf(message);
        android.util.Log.d(getClassName(), msg);
        writeLogtoFile("debug", getClassName(), msg);
    }

    public static void debug(Date message) {
        String msg = DateUtils.toString(message);
        android.util.Log.d(getClassName(), msg);
        writeLogtoFile("debug", getClassName(), msg);
    }

    public static void debug(Object message) {
        String msg = String.valueOf(message);
        android.util.Log.d(getClassName(), msg);
        writeLogtoFile("debug", getClassName(), msg);
    }

    public static void info(String message) {
        String msg = String.valueOf(message);
        android.util.Log.i(getClassName(), msg);
        writeLogtoFile("info", getClassName(), msg);
    }

    public static void info(String message, Object... args) {
        String msg = String.valueOf(message);
        String format = MessageFormat.format(msg, args);
        android.util.Log.i(getClassName(), format);
        writeLogtoFile("info", getClassName(), format);
    }

    public static void info(Number message) {
        String msg = String.valueOf(message);
        android.util.Log.i(getClassName(), msg);
        writeLogtoFile("info", getClassName(), msg);
    }

    public static void info(Date message) {
        String msg = DateUtils.toString(message);
        android.util.Log.i(getClassName(), msg);
        writeLogtoFile("info", getClassName(), msg);
    }

    public static void info(Object message) {
        String msg = String.valueOf(message);
        android.util.Log.i(getClassName(), msg);
        writeLogtoFile("info", getClassName(), msg);
    }

    public static void error(String message) {
        android.util.Log.e(getClassName(), message);
        writeLogtoFile("error", getClassName(), message);
    }

    public static void error(Number message) {
        String msg = String.valueOf(message);
        android.util.Log.e(getClassName(), msg);
        writeLogtoFile("error", getClassName(), msg);
    }

    public static void error(Date message) {
        String msg = DateUtils.toString(message);
        android.util.Log.e(getClassName(), msg);
        writeLogtoFile("error", getClassName(), msg);
    }

    public static void error(Object message) {
        String msg = String.valueOf(message);
        android.util.Log.e(getClassName(), msg);
        writeLogtoFile("error", getClassName(), msg);
    }

    public static void error(Throwable e) {
        android.util.Log.e(getClassName(), String.valueOf(e.getMessage()));
        e.printStackTrace();

        writeLogtoFile("error", getClassName(), e);
    }

    private static String getClassName() {
        StackTraceElement[] stackTraceElements = Thread.getAllStackTraces().get(Thread.currentThread());
        return stackTraceElements[5].getClassName() + "." + stackTraceElements[5].getMethodName() + "("
                + stackTraceElements[5].getLineNumber() + ")";
    }

    static int i = 0;

    private static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFile = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype
                + "[" + tag + "]" + text;
        File file = new File(FileUtils.getExternalStorageDirectory(pack), needWriteFile
                + (tag.equals("error") ? "error" : "") + "log.txt");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            PrintWriter writer = new PrintWriter(filerWriter);
            writer.write(needWriteMessage);
            writer.write("\r\n");
            writer.flush();
            writer.close();
            filerWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeLogtoFile(String mylogtype, String tag, Throwable e) {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFile = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype
                + "    " + tag + "    " + e.getMessage();
        File file = new File(FileUtils.getExternalStorageDirectory(pack), needWriteFile
                + "log.txt");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            PrintWriter writer = new PrintWriter(filerWriter);
            writer.write(needWriteMessage);
            writer.write("\r\n");
            e.printStackTrace(writer);
            writer.write("\r\n");
            writer.flush();
            writer.close();
            filerWriter.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     */
    public static void delFile() {// 删除日志文件
        String needDelFiel = logfile.format(getDateBefore());
        File file = new File(FileUtils.getExternalStorageDirectory(pack), needDelFiel + "log.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE)
                - 20);
        return now.getTime();
    }

    public static void init(Context context) {
        pack = "/" + StringUtils.substringAfterLast(context.getApplicationContext().getPackageName(), ".");
//        info("cccccccccccccccccccccccccccccccccccccccUUU:" + pack);
    }
}
