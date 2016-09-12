package controllers.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Nobosny on 1/12/2015.
 * controllers.helpers
 */
public class Decoder {

    public static Date dateDecoderOr(String[] longStr, Date def) {
        if ((longStr != null) && (longStr.length > 0)) {
            return new Date(Long.parseLong(longStr[0]));
        } else {
            return def;
        }
    }

    public static Date dateStrDecoderOr(String dateStr, Date def) {
        if ((dateStr != null) && (dateStr.length() > 0)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date result = def;
            try {
                result = df.parse(dateStr);
            } catch (Exception ex) {
                return def;
            }
            return result;
        } else {
            return def;
        }
    }

    public static Date dateStrDecoderOr(String dateStr, Date def, boolean starting) {
        if ((dateStr != null) && (dateStr.length() > 0)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date result = def;
            try {
                result = df.parse(dateStr);
                Calendar cal = new GregorianCalendar();
                cal.setTime(result);
                if (starting) {
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                            0, 0, 0);
                } else {
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                            23, 59, 59);
                }
                result = cal.getTime();
            } catch (Exception ex) {
                return def;
            }
            return result;
        } else {
            return def;
        }
    }

    public static Integer booleanIntDecoderOr(String[] booleanStr, Integer def) {
        if ((booleanStr != null) && (booleanStr.length > 0)) {
            if (booleanStr[0].equals("false")) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return def;
        }
    }

    public static String stringDecoderOr(String[] stringStr, String def) {
        if ((stringStr != null) && (stringStr.length > 0)) {
            return stringStr[0];
        } else {
            return def;
        }
    }

    public static Double doubleDecoderOr(String[] doubleStr, Double def) {
        if ((doubleStr != null) &&(doubleStr.length > 0)) {
            return Double.parseDouble(doubleStr[0]);
        } else {
            return def;
        }
    }
}
