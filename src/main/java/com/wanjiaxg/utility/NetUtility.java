package com.wanjiaxg.utility;

import com.wanjiaxg.http.WebClient;
import com.wanjiaxg.http.WebCookieJar;

public final class NetUtility {

    private static WebClient client;

    static {
        client = new WebClient();
        client.setCookieJar(WebCookieJar.getInstance());
    }

    public static String downloadString(String url){
        return client.load(url).open().getBody();
    }

    public static boolean downloadFile(String url, String file){
        return client.load(url).open().saveFile(file);
    }

}
