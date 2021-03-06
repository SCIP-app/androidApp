
package scip.app.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Allie on 7/24/2015.
 */
public class DateUtil {

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static Date getDateFromString(String date) {
        if (getDateFromString(date, "dd-MM-yyyy") == null)
            return getDateFromString(date, "dd/MM/yyyy");
        else
            return getDateFromString(date, "dd-MM-yyyy");

    }

    public static Date getDateFromString(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getStringFromDate(Date date){
        java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime( date );
        if (cal.YEAR == 0)
            cal.add( GregorianCalendar.YEAR, 2000 );
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(cal.getTime() );
    }

    public static String getStringFromMMddyyy(Date date){
        java.util.Calendar cal = GregorianCalendar.getInstance();
        cal.setTime( date );
        if (cal.YEAR == 0)
            cal.add( GregorianCalendar.YEAR, 2000 );
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(cal.getTime() );
    }
}
