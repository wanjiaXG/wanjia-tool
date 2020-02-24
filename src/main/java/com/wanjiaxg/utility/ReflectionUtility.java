package com.wanjiaxg.utility;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtility {

    public static boolean setValue(Object object, String fieldName, Object newValue){
        boolean success = false;
        try {
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, newValue);
            success = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    public static Object invokeMethod(Object object, String methodName, Object...args) {
        if(object == null) return null;
        if(methodName == null) return null;

        Object result = null;
        Method method = null;
        Class<?> clazz = object.getClass();
        try{
            method = clazz.getDeclaredMethod(methodName);
        }catch (Exception e){
            e.printStackTrace();
            if(clazz.getSuperclass() != null){
                try {
                    method = clazz.getSuperclass().getDeclaredMethod(methodName);
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if(method != null){
            try {
                result = method.invoke(args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
