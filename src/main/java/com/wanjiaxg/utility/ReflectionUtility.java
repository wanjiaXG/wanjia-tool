package com.wanjiaxg.utility;

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

    public static Method getMethod(Class clazz, String methodName, Class...args) {
        if(clazz == null || methodName == null) return null;

        Method method = null;
        try{
            method = clazz.getDeclaredMethod(methodName, args);
        }catch (Exception e){
            if(clazz.getSuperclass() != null){
                try {
                    method = clazz.getSuperclass().getDeclaredMethod(methodName, args);
                } catch (NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            }else {
                e.printStackTrace();
            }
        }
        return method;
    }

    public static Object invokeMethod(Object object, Method method, Object... args){
        if(object == null || method == null) return null;

        Object result = null;
        try {
            method.setAccessible(true);
            result = method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
