package com.wanjia.test;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.NetUtility;
import sun.nio.ch.Net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetTest {
    public static void main(String[] args) throws IOException {
        System.out.println(NetUtility.downloadFile("http://dl.osuyun.com/beatmaps/0/1","1.osz"));
    }
}
