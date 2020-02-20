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

    public static Object callPrivateMethod(Object object, String methodName, Object...args) {
        Object result = null;
        try{
            Class<?> clazz = object.getClass();
            Method method = clazz.getDeclaredMethod(methodName);
            method.setAccessible(true);
            result = method.invoke(object, args);
        }catch (Exception e){

        }
        return result;
    }
}
