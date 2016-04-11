package works.tonny.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import works.tonny.mobile.utils.Log;

/**
 * Created by tonny on 2015/7/4.
 */
public class Application {

    public static final String LOGINED_USER_USERNAME = "logined_user.username";
    public static final String LOGINED_USER_PASSWORD = "logined_user.password";
    private static final String LOGINED_USER_NAME = "logined_user.name";
    private static final String LOGINED_USER_NICKNAME = "logined_user.nickname";
    private static final String LOGINED_USER_ID = "logined_user.id";
    private static final String LOGINED_USER_HEADER = "logined_user.header";
    private static final String LOGINED_USER_LOGINTIME = "logined_user.logintime";
    private static SharedPreferences preferences;
    private static Resources resources;

    private static User user;

    private static String host;
    private static PackageInfo packageInfo;

    private static Version version;

    static void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        resources = context.getResources();
        packageInfo = getPackageInfo(context);
        version = new Version(getVersionName());
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static User getUser() {
        if (user == null && preferences != null) {
            String username = preferences.getString(LOGINED_USER_USERNAME, null);
            String password = preferences.getString(LOGINED_USER_PASSWORD, null);

            if (username == null) {
                return null;
            }
            user = new User(username, password);
            user.setName(preferences.getString(LOGINED_USER_NAME, null));
            user.setHeader(preferences.getString(LOGINED_USER_HEADER, null));
            Log.info(user.getHeader());
            user.setNickname(preferences.getString(LOGINED_USER_NICKNAME, null));
            user.setId(preferences.getString(LOGINED_USER_ID, null));
            user.setLastLoginTime(new Date(preferences.getLong(LOGINED_USER_LOGINTIME, 0)));
            Map<String, ?> all = preferences.getAll();
            Map<String, String> properties = new HashMap<>();
            for (String s : all.keySet()) {
                if (s.startsWith("map_")) {
                    properties.put(s.substring(4), (String) all.get(s));
                }
            }
            user.setProperties(properties);
        }
        return user;
    }

    /**
     * 注册登录用户信息
     *
     * @param username
     * @param password
     */
    public static void login(String username, String password) {
        user = new User(username, password);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(LOGINED_USER_USERNAME, username);
        edit.putString(LOGINED_USER_PASSWORD, password);
        edit.putString(LOGINED_USER_NAME, "");
        edit.putString(LOGINED_USER_NICKNAME, "");
        edit.putString(LOGINED_USER_ID, "");
        edit.putString(LOGINED_USER_HEADER, "");
        edit.putLong(LOGINED_USER_LOGINTIME, 0);
        edit.commit();
    }


    public static void saveUser(User user) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(LOGINED_USER_USERNAME, user.getUsername());
        edit.putString(LOGINED_USER_PASSWORD, user.getPassword());
        edit.putString(LOGINED_USER_NAME, user.getName());
        edit.putString(LOGINED_USER_NICKNAME, user.getNickname());
        edit.putString(LOGINED_USER_ID, user.getId());
        edit.putString(LOGINED_USER_HEADER, user.getHeader());
        if (user.getProperties() != null && !user.getProperties().isEmpty()) {
            Set<String> strings = user.getProperties().keySet();
            for (String string : strings) {
                edit.putString("map_" + string, user.getProperties().get(string));
            }
        }
        if (user.getLastLoginTime() != null)
            edit.putLong(LOGINED_USER_LOGINTIME, user.getLastLoginTime().getTime());
        edit.commit();
    }

    public static void logout() {
        user = null;
        SharedPreferences.Editor edit = preferences.edit();
        edit.remove(LOGINED_USER_USERNAME);
        edit.remove(LOGINED_USER_PASSWORD);
        edit.commit();
    }


    public static SharedPreferences getPreferences() {
        return preferences;
    }


    public static void saveToSharedPreferences(String name, String value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(name, value);
        edit.commit();
    }


    public static void saveToSharedPreferences(String name, Boolean value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(name, value);
        edit.commit();
    }


    public static void setHost(int id) {
        host = resources.getString(id);
    }


    public static String getUrl(int id) {
        String url = resources.getString(id);
        return getUrl(url);
    }

    public static String getUrl(String url) {
        if (url.toLowerCase().startsWith("http://")) {
            return url;
        }
        return host + url;
    }


    public static String getVersionName() {
        return packageInfo.versionName;
    }

    /**
     * h获取版本信息
     *
     * @return
     */
    public static Version getVersion() {
        return version;
    }

    //版本号
    public static int getVersionCode() {
        return packageInfo.versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

//            Log.info(context.getPackageName() + pi.versionName);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }


}
