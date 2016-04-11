package works.tonny.mobile.autobackup;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2016/1/20.
 */
public class BackupConfig implements Serializable {
    static final long serialVersionUID = 4L;
    private String host;

    private String folder;

    private String username;

    private String password;

    private long last = 0;

    /**
     * 待备份本地的文件夹
     */
    private List<File> locals = new ArrayList<>();

    /**
     * 扫描后要备份的所有文件，空的文件夹不备份
     */
    private Map<File, List<File>> todo = new HashMap<>();

    /**
     * 所有已完成的文件，不会再备份
     */
    private Map<File, Long> finished = new HashMap<>();

    /**
     * 失败的备份，下次即使文件存在也会备份
     */
    private List<File> failed = new ArrayList<>();


    public BackupConfig() {

    }

    public BackupConfig(String host, String folder, String username, String password) {
        this.host = host;
        this.folder = folder;
        this.username = username;
        this.password = password;
    }


    /**
     * 清除缓存
     */
    public void reset() {
        finished.clear();
        failed.clear();
    }


    /**
     * 添加本地文件或文件夹
     *
     * @param file
     */
    public void addLocal(File file) {
        if (locals == null) {
            locals = new ArrayList<>();
        }
        locals.add(file);
    }


    /**
     * 完成一个文件的备份，文件没有失败
     *
     * @param file
     */
    public void finish(File file) {
        finished.put(file, file.lastModified());
        last = System.currentTimeMillis();
    }

    /**
     * 文件备份失败
     *
     * @param file
     */
    public void fail(File file) {
        if (failed.contains(file)) {
            return;
        }
        failed.add(file);
    }

    @Override
    public String toString() {
        try {
            StringBuilder b = new StringBuilder("smb://");
            if (!StringUtils.isEmpty(username)) {
                b.append(URLEncoder.encode(username, "utf-8")).append(":").append(URLEncoder.encode(password, "utf-8")).append("@");
            }
            b.append(host).append("/").append(folder.replaceAll("\\\\", "/")).append(StringUtils.isEmpty(folder) ? "" : "/");

            return b.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 扫描要备份的文件
     */
    public void scan() {
        todo.clear();
        for (File file : locals) {
            addDir(file, file);
        }
    }

    private void addDir(File base, File file) {
        if (!file.isDirectory()) {
            Log.info(file + ":" + finished.containsKey(file));
            if (finished.containsKey(file) && finished.get(file) == file.lastModified()) {
                return;
            }

            if (todo.get(base) == null) {
                todo.put(base, new ArrayList<File>());
            }
            todo.get(base).add(file);
            return;
        }
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return new Long(lhs.lastModified()).compareTo(rhs.lastModified());
            }
        });
        for (File file1 : files) {
            addDir(base, file1);
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }


    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public List<File> getLocals() {
        return locals;
    }

    public void setLocals(List<File> locals) {
        this.locals = locals;
    }

    public Map<File, List<File>> getTodo() {
        return todo;
    }

    public void setTodo(Map<File, List<File>> todo) {
        this.todo = todo;
    }

    public List<File> getFailed() {
        return failed;
    }

    public int getFinished() {
        int total = 0;
        for (File file : finished.keySet()) {
            total += finished.get(file);
        }
        return total;
    }

}
