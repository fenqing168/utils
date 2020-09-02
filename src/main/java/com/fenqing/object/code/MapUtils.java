package com.fenqing.object.code;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fenqing
 */
public class MapUtils {

    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>(8);
    }

}
