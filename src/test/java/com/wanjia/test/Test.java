package com.wanjia.test;

import com.wanjiaxg.http.MediaTypeSet;
import com.wanjiaxg.utility.FileUtility;
import okhttp3.MediaType;

import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException {
        MediaType type = MediaTypeSet.getMediaTypeBySuffix(".apk");
        System.out.println(type);
    }
}
