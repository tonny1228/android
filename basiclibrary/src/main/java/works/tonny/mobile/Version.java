package works.tonny.mobile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.StrBuilder;


/**
 * 版本号管理
 * Created by tonny on 2016/3/15.
 */
public class Version implements Comparable<String> {

    public static final String DOT = ".";
    /**
     * 主版本
     */
    private int major;
    /**
     * 次版本
     */
    private int minor = -1;
    /**
     * 发布版本
     */
    private int build = -1;
    /**
     * 修订版本
     */
    private String revision;


    public Version(String version) {
        parse(version);
    }

    public Version(int major, int minor, int build, String revision) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.revision = revision;
    }

    private void parse(String version) {
        String[] split = StringUtils.split(".");
        if (split == null || split.length == 0) {
            return;
        }
        if (split.length > 0) {
            major = NumberUtils.toInt(split[0]);
        }
        if (split.length > 1) {
            minor = NumberUtils.toInt(split[1]);
        }
        if (split.length > 2) {
            build = NumberUtils.toInt(split[2]);
        }
        if (split.length > 3) {
            revision = split[3];
        }
    }


    @Override
    public String toString() {
        StrBuilder b = new StrBuilder();
        b.append(major);
        if (minor > -1) {
            b.append(DOT);
            b.append(minor);
        }
        if (build > -1) {
            b.append(DOT).append(build);
        }
        if (StringUtils.isNotEmpty(revision)) {
            b.append(DOT).append(revision);
        }

        return b.toString();
    }

    /**
     * 比较版本号
     *
     * @param another
     * @return
     */
    @Override
    public int compareTo(String another) {
        Version dust = new Version(another);
        if (major < dust.major) {
            return -1;
        }
        if (major > dust.major) {
            return 1;
        }
        if (minor < dust.minor) {
            return -1;
        }
        if (minor > dust.minor) {
            return 1;
        }
        if (build < dust.build) {
            return -1;
        }
        if (build > dust.build) {
            return 1;
        }

        if (revision == null && dust.revision == null) {
            return 0;
        }

        if (revision == null && dust.revision != null) {
            return 1;
        }

        return revision.compareTo(dust.revision);
    }
}
