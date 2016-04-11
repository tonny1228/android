package works.tonny.mobile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tonny on 2015/7/4.
 */
public class User {
    private String id;

    private String username;

    private String password;

    private String name;

    private String nickname;

    private Date lastLoginTime;

    private Map<String, String> properties;

    private String header;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void addProperty(String key, String value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    public String getProperty(String key) {
        if (properties == null) {
            return null;
        }
        return properties.get(key);
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
