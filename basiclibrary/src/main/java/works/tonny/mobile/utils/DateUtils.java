package works.tonny.mobile.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tonny on 2015/7/11.
 */

public class DateUtils {
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String[] MONTH_NAMES_CN = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月",
            "十二月"};

    private static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

    private static final String[] WEEK_NAMES_CN = {"一", "二", "三", "四", "五", "六", "日"};

    private static final String[] WEEK_NAMES = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday"};

    private DateUtils() {
    }

    /**
     * 格式化日期，传递日期对象，按照默认格式进行格式化<br>
     * 即位yyyy-MM-dd HH:mm:ss
     *
     * @param d 日期
     * @return 字符串
     */
    public static String toString(Date d, String s) {
        SimpleDateFormat formatter = new SimpleDateFormat(s);
        String str = formatter.format(d);
        return str;
    }

    /**
     * 格式化日期，传递日期对象，按照默认格式进行格式化<br>
     * 即位yyyy-MM-dd HH:mm:ss
     *
     * @param d      日期
     * @param locale 区域
     * @return 字符串
     */
    public static String toString(Date d, String s, Locale locale) {
        SimpleDateFormat formatter = new SimpleDateFormat(s, locale);
        String str = formatter.format(d);
        return str;
    }

    /**
     * 格式化日期，传递格式，对当前时间进行格式化<br>
     *
     * @param s 格式串
     * @return 字符串
     */
    public static String toString(String s) {
        return toString(new Date(), s);
    }

    /**
     * 格式化日期，传递日期对象，按照默认格式进行格式化<br>
     * 即位yyyy-MM-dd HH:mm:ss
     *
     * @param d 日期
     * @return 字符串
     */
    public static String toString(Date d) {
        return toString(d, DEFAULT_PATTERN);
    }

    /**
     * 得到当前的日期
     *
     * @return String得到字串格式的当前日期
     */

    public static String currentDate() {
        return toString(new Date(), DEFAULT_PATTERN);
    }

    /**
     * 将日期字符串按默认格式转换为日期
     *
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date toDate(String dateStr) {
        return toDate(dateStr, DEFAULT_PATTERN);
    }

    /**
     * 将日期字符串按指定格式转换为日期
     *
     * @param dateStr 日志字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date toDate(String dateStr, String pattern) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(pattern);
        Date data = null;
        try {
            data = bartDateFormat.parse(dateStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    /**
     * 将日期字符串按指定格式转换为日期
     *
     * @param dateStr 日志字符串
     * @param pattern 日期格式
     * @param locale  区域
     * @return 日期
     */
    public static Date toDate(String dateStr, String pattern, Locale locale) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(pattern, locale);
        Date data = null;
        try {
            data = bartDateFormat.parse(dateStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return data;
    }

}
