package cn.com.jrj.vtmatch.basicmatch.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 日期工具类
 * </p>
 *
 * @author xinsheng.dong
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateToolkit {

    public static final String FORMAT_YYYYMMDD_NO_SYMBOL = "yyyyMMdd";

    public static final String FORMAT_HHMMSS_NO_SYMBOL = "HHmmss";

    public static final String FORMAT_HHMM_NO_SYMBOL = "HHmm";

    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";

    public static final String FORMAT_YYYYMMDD_SLASH = "yyyy/MM/dd";

    public static final String FORMAT_YYMMDD_SLASH = "yy/MM/dd";

    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String FORMAT_YYYYMMDDHHMMSS_WITH_SYMBOL = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_MMDDHHMMSS_WITH_SYMBOL = "MM-dd HH:mm:ss";

    public static final String FORMAT_YYYYMMDDHHMM_WITH_SYMBOL = "yyyy-MM-dd HH:mm";

    public static final String FORMAT_MMDDHHMM_WITH_SYMBOL = "MM-dd HH:mm";

    public static final String FORMAT_MMDD_WITH_SYMBOL = "MM-dd";

    public static final String FORMAT_MMDD_SLASH = "M/d";

    public static final String FORMAT_HHMM_WITH_SYMBOL = "HH:mm";

    public static final String FORMAT_YYYYMMDDHHMMSS_HUMAN = "yyyy年MM月dd日 HH时mm分ss秒";

    public static final String FORMAT_YYYYMMDDHHMM_HUMAN = "yyyy年MM月dd日 HH时mm分";

    public static final String FORMAT_YYYYMMDD_HUMAN = "yyyy年MM月dd日";

    public static final String FORMAT_MMDDHHMM_HUMAN = "MM月dd日 HH时mm分";

    public static final String FORMAT_MMDD_HUMAN = "MM月dd日";

    public static final String FORMAT_MD_HUMAN = "M月d日";

    private static final String FORMAT_TODAY = "今天";

    private static final String FORMAT_YESTERDAY = "昨天";

    public static final String FORMAT_YYYY = "yyyy";

    public static final String FORMAT_YYYYMM = "yyyyMM";

    public static Date getNow() {
        return new Date();
    }

    public static String format(long date, String format) {
        return format(new Date(date), format);
    }

    public static String format(Date date, String format) {
        if (date == null || StringUtils.isEmpty(format)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatHumanDate(Date date, String defaultDateFormat, String timeFormat) {

        if (date == null || StringUtils.isEmpty(defaultDateFormat)) {
            return null;
        }

        Calendar target = Calendar.getInstance();
        target.setTime(date);

        Calendar today = Calendar.getInstance();

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        if (target.get(Calendar.YEAR) == today.get(Calendar.YEAR) && target.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return FORMAT_TODAY.concat(" ").concat(format(date, timeFormat));
        } else if (target.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && target.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return FORMAT_YESTERDAY.concat(" ").concat(format(date, timeFormat));
        } else {
            String temp = format(date, defaultDateFormat);
            if (temp == null) {
                return format(date, timeFormat);
            } else {
                return temp.concat(" ").concat(format(date, timeFormat));
            }
        }

    }

    public static String format(String date, String oldFormat, String newFormat) {
        return format(parse(date, oldFormat), newFormat);
    }

    public static String getNowFormatyyyyMMddHHmmss() {
        return format(getNow(), FORMAT_YYYYMMDDHHMMSS);
    }

    public static String getNowFormatyyyyMMddHHmmssHuman() {
        return format(getNow(), FORMAT_YYYYMMDDHHMMSS_HUMAN);
    }

    public static String getNowFormatyyyyMMdd() {
        return format(getNow(), FORMAT_YYYYMMDD);
    }

    public static String getNowFormatyyyyMMddNoSymbol() {
        return format(getNow(), FORMAT_YYYYMMDD_NO_SYMBOL);
    }

    public static String getNowFormatyyyyMMddHHmmssNoSymbol() {
        return format(getNow(), FORMAT_YYYYMMDDHHMMSS_WITH_SYMBOL);
    }

    public static int getFormatyyyyMMddNoSymbol(Date date) {
        return Integer.valueOf(format(date, FORMAT_YYYYMMDD_NO_SYMBOL));
    }

    public static Date parse(String date, String format) {
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(format)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public static Date addYears(Date date, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, years);
        return c.getTime();
    }

    public static long daysBetween(Date dateFrom, Date dateTo) {
        if (dateFrom == null || dateTo == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(dateFrom.toInstant(), dateTo.toInstant());
    }

    public static boolean checkFormat(String date, String format) {
        return parse(date, format) != null;
    }

    /**
     * 有一个小弊端，跨年的时候会出异常
     */
    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    public static long mouthEndTime() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.MILLISECOND, 59);
        return ca.getTimeInMillis() - System.currentTimeMillis();
    }


    public static long daysOfTwo(Long end, Long start) {
        if (end > start) {
            return (end - start) / (24 * 60 * 60 * 1000);
        }
        return 0;
    }

    public static Date dayOfEnd(Date date) {
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 23);
        start.set(Calendar.MINUTE, 59);
        start.set(Calendar.MILLISECOND, 59);
        return start.getTime();
    }
}
