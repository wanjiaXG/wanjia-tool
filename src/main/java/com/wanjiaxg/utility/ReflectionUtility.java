package com.wanjiaxg.utility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("ALL")
public final class ReflectionUtility {

    /**
     * 设置对象里边某个字段的值
     * @param object        对象引用
     * @param fieldName     字段名
     * @param newValue      字段值
     * @return
     */
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

    /**
     * 获取类中的某个方法
     * @param clazz         class对象
     * @param methodName    方法名
     * @param args          方法形参数组
     * @return
     */
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

    /**
     * 调用对象的方法
     * @param object    对象引用
     * @param method    方法引用
     * @param args      形参数组
     * @return          方法返回值
     */
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
