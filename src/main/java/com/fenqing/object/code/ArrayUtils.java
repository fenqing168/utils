package com.fenqing.object.code;

/**
 * @author fenqing
 */
public class ArrayUtils {

    public static byte[] openBox(Byte[] bytes){
        byte[] bytes1 = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes1[i] = bytes[i];
        }
        return bytes1;
    }

    public static boolean contains(int[] arr, int val){
        for (int i : arr) {
            if (i == val){
                return true;
            }
        }
        return false;
    }

}
