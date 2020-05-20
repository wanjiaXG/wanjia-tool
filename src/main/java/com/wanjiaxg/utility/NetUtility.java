package com.wanjiaxg.utility;

import com.wanjiaxg.http.WebClient;
import com.wanjiaxg.http.WebCookieJar;

public final class NetUtility {

    private static WebClient client;

    private static WebCookieJar cookieJar;

    static {
        client = new WebClient();
        cookieJar = new WebCookieJar();
        client.setCookieJar(cookieJar);
    }

    public static String downloadString(String url){
        return client.load(url).open().getBody();
    }

    public static boolean downloadFile(String url, String file){
        return client.load(url).open().saveFile(file);
    }

}
