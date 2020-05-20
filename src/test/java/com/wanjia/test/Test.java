package com.wanjia.test;

import com.wanjiaxg.http.MediaTypeSet;
import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.NetUtility;
import okhttp3.MediaType;

import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException {
        String s = NetUtility.downloadString("http://www.baidu.com");
        System.out.println(s);
    }
}
