package utn.tdm.meegos.util;

import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil
{
    private static SimpleDateFormat dateFormat;
    
    static {
        DateUtil.dateFormat = (SimpleDateFormat)DateFormat.getDateInstance();
    }
    
    public static Calendar getCalendar(final String timestamp, final String pattern) {
        Date date = null;
        Calendar time = Calendar.getInstance();
        DateUtil.dateFormat.applyPattern(pattern);
        try {
            date = DateUtil.dateFormat.parse(timestamp);
            time.setTime(date);
        }
        catch (Exception e) {
            time = null;
        }
        return time;
    }
    
    public static String getCalendarAsString(final Calendar date, final String pattern) {
        DateUtil.dateFormat.applyPattern(pattern);
        return DateUtil.dateFormat.format(date.getTime());
    }
}
