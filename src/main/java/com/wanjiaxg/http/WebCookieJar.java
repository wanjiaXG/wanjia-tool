package com.wanjiaxg.http;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WebCookieJar implements CookieJar {

    private static WebCookieJar cookieJar;

    private Map<String, Map<String, Cookie>> cookies;

    private WebCookieJar(){
        this.cookies = new HashMap<>();
    }

    public static WebCookieJar getInstance(){
        if(cookieJar == null)
            cookieJar = new WebCookieJar();
        return cookieJar;
    }
    @NotNull
    @Override
    public synchronized List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        List<Cookie> list = new ArrayList<>();
        for(Map.Entry<String, Map<String, Cookie>> entry : this.cookies.entrySet()){
            String key = entry.getKey();
            if(httpUrl.toString().contains(key)){
                Iterator<Map.Entry<String, Cookie>> iterator = entry.getValue().entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, Cookie> item = iterator.next();
                    if(item.getValue().expiresAt() > System.currentTimeMillis()){
                        list.add(item.getValue());
                    }else {
                        iterator.remove();
                    }
                }
            }
        }
        return list;
    }

    @Override
    public synchronized void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
        for(Cookie cookie : list){
            String key = cookie.domain();
            String path = cookie.path();
            if(!"/".equals(path)){
                key += path;
            }
            if(cookie.expiresAt() > System.currentTimeMillis()){
                Map<String, Cookie> item = null;
                if(this.cookies.containsKey(key)){
                    item = this.cookies.get(key);
                }else {
                    item = new HashMap<>();
                }
                item.put(cookie.name(), cookie);
                this.cookies.put(key, item);
            }
        }
    }

    public String getCookieValue(String url, String key){
        String result = null;
        if(url != null && key != null){
            try {
                for (Cookie cookie : loadForRequest(Objects.requireNonNull(HttpUrl.parse(url)))){
                    if(key.equals(cookie.name())){
                        result = cookie.value();
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public String getCookieByUrl(String url){
        StringBuilder sb = new StringBuilder();
        if(url != null){
            try {
                for (Cookie cookie : loadForRequest(Objects.requireNonNull(HttpUrl.parse(url)))){
                    sb.append(cookie.name())
                            .append("=")
                            .append(cookie.value())
                            .append(";");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public synchronized void removeCookieByUrl(String url){
        if(url != null){
            this.cookies.entrySet().removeIf(entry -> url.contains(entry.getKey()));
        }
    }
}
