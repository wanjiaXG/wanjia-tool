package com.wanjia.test;

import com.wanjiaxg.http.WebClient;
import com.wanjiaxg.http.WebResponse;
import com.wanjiaxg.utility.NetUtility;

import java.io.IOException;
import java.util.Random;

public class NetTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        WebClient client = new WebClient();
        WebResponse hhh = client.load("https://osu.wanjiaxg.com/xxx").open();
        System.out.println(hhh.getResponse());
    }

}
