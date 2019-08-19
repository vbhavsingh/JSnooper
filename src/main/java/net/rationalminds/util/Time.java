package net.rationalminds.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class Time {

    private static SimpleDateFormat dfrm;

    static {
        dfrm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        dfrm.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
     * get currentExecutionTime
     */
    public static String getTime() {
        return dfrm.format(new Date());
    }

    /**
     * get currentExecutionTime
     */
    public static String getTime(Date date) {
        return dfrm.format(date);
    }
    
    /**
     * 
     * @return 
     */
    public static String getTimeZone() {
        TimeZone tz = Calendar.getInstance().getTimeZone();
        return tz.getDisplayName(true, TimeZone.SHORT, Locale.ENGLISH);
    }

}
