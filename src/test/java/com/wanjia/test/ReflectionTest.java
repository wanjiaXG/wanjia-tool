package com.wanjia.test;

import com.wanjiaxg.utility.ReflectionUtility;

import java.io.File;

public class ReflectionTest {
    public static void main(String[] args) {
        File file = new File("C:\\git.exe");
        Object length = ReflectionUtility.invokeMethod(file, "length");
        System.out.println(length);
    }
}
