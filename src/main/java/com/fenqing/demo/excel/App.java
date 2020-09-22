package com.fenqing.demo.excel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class App {

    public static void test(String... a){
        System.out.println(Arrays.toString(a));
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method string = App.class.getDeclaredMethod("test", String[].class);
        String[] a = {"1", "2"};
        test(a);
        string.invoke(null, (Object)a);
    }
}
