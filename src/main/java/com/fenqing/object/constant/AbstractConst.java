package com.fenqing.object.constant;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author fenqing
 */
public class AbstractConst {

    public static final Pattern PATTERN = Pattern.compile("(?<=\\[)(\\d+)(?=])");
    private static Method MAP_GET_METHOD;

    static {
        try {
            MAP_GET_METHOD = Map.class.getMethod("get", Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Method getMapGetMethod() {
        return MAP_GET_METHOD;
    }

}
