package com.fenqing.time.code;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fenqing
 */
public class TimeUtils {

    private final static Map<String, DateFormat> DATE_FORMAT_MAP = new HashMap<>();

    public static DateFormat getDateFormat(String format) {
        DateFormat dateFormat = DATE_FORMAT_MAP.get(format);
        if(dateFormat == null){
            dateFormat = new SimpleDateFormat(format);
            DATE_FORMAT_MAP.put(format, dateFormat);
        }
        return dateFormat;
    }
}
