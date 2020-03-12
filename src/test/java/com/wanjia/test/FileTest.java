package com.wanjia.test;

import com.wanjiaxg.utility.ZipUtility;

import java.io.*;

public class FileTest {

    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("cc.zip");
        ZipUtility.make("C:\\Users\\Administrator\\source\\repos\\ImageDemo", fos);
        fos.close();
    }
}
