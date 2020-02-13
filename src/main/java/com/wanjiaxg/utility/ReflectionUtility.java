package com.wanjiaxg.utility;

import java.lang.reflect.Field;

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

}
