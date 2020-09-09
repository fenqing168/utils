package com.fenqing.demo.excel;


import com.fenqing.object.code.DataUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class App {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Runnable runnable = () -> {
            System.out.println(1);
        };
        System.out.println(runnable.getClass());
//        new Thread(runnable).start();
        Method[] declaredMethods = App.class.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println(declaredMethod.getName());
        }
    }
    private static void  lambda$main$0(){
        final class demo{
            
        }
    }

}
