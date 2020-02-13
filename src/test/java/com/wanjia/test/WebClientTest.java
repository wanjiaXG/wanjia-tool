package com.wanjia.test;

import com.wanjiaxg.http.WebClient;
import com.wanjiaxg.http.IWebSaveFileCallback;
import com.wanjiaxg.utility.RegexUtility;
import okhttp3.*;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WebClientTest {

    public static void main(String[] args) {
        WebClient client = WebClient.getInstance();
        String url = "https://qd.myapp.com/myapp/qqteam/pcqq/PCQQ2020.exe";
        client.load(url).openAsync(RegexUtility.getFirstResult(url, "(?!.*/).+"), new IWebSaveFileCallback() {
            DecimalFormat df = new DecimalFormat("0.00");
            @Override
            public void onReady(int stateCode, Map<String, List<String>> headerFields, long contentLength) {
                System.out.println("文件大小: " + df.format((contentLength/1024.0/1024.0)) + "MB");
                totalLength = contentLength;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("正在下载: " + df.format((currentLength/1024.0/1024.0)) + "MB/" + df.format((totalLength/1024.0/1024.0)) + "MB, 速度: " + (length/1024) + "kb/s");
                        length = 0;
                    }
                },0,1000);
            }

            @Override
            public void onReading(int length) {
                currentLength+=length;
                this.length+=length;
            }

            @Override
            public void onSuccess() {
                System.out.println("下载完成");
                timer.cancel();
            }

            @Override
            public void onError(String message) {
                System.out.println("下载失败");
                timer.cancel();
            }

            private long currentLength;

            private long totalLength;

            private long length;

            private Timer timer = new Timer();

        });
    }

    @Test
    public void Test02() throws IOException {
        String url = "http://www.baidu.com";
        WebClient webClient = WebClient.getInstance();
        OkHttpClient okHttpClient = new OkHttpClient();

        for(int i = 0; i < 20; i++){
            long start = System.currentTimeMillis();
            String body = webClient.load(url).open().body();
            long end = System.currentTimeMillis();
            System.out.println("WebClient: " + (end - start) + "ms");
        }


        for(int i = 0; i < 20; i++){
            long start = System.currentTimeMillis();
            String body = okHttpClient.newCall(new Request.Builder().url(url).build()).execute().body().string();
            long end = System.currentTimeMillis();
            System.out.println("OkHttpClient: " + (end - start) + "ms");
        }
    }

    @Test
    public void test03() throws IOException {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");
    }
}
