package com.wanjia.test;

import com.wanjiaxg.utility.RegexUtility;

public class RegexTest {
    public static void main(String[] args) {
        String url = "https://qd.myapp.com/myapp/qqteam/pcqq/PCQQ2020.exe.html";
        System.out.println(RegexUtility.getFilePath(url));
    }
}
