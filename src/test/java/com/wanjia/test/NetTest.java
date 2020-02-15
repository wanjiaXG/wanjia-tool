package com.wanjia.test;

import com.wanjiaxg.utility.NetUtility;

public class NetTest {
    public static void main(String[] args) {
        System.out.printf(NetUtility.downloadString("https://api.github.com/repos/ppy/osu/releases"));
    }
}
