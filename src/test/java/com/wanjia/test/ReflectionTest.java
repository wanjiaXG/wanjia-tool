package com.wanjia.test;

import com.wanjiaxg.utility.ReflectionUtility;

import java.io.File;
import java.lang.reflect.Method;

public class ReflectionTest {
    public static void main(String[] args) {
        File file = new File("C:\\git.exe");
        B b = new B();
        Method method = ReflectionUtility.getMethod(B.class, "setName", String.class, String.class);
        Object length = ReflectionUtility.invokeMethod(b, method,"wanjia", "aijnaw");
        System.out.println(b.getName());
    }
}
class A{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String firstName, String lastName){
        this.name = firstName + " " + lastName;
    }
}

class B extends A{

}