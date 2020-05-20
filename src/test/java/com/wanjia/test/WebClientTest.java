package com.wanjia.test;

import com.wanjiaxg.http.WebClient;

import java.io.FileOutputStream;
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
        client.load(url).saveAsync(new IWebSaveFileCallback() {
            DecimalFormat df = new DecimalFormat("0.00");
            @Override
            public void onReady(int stateCode, Map<String, List<String>> headerFields, long contentLength) throws IOException {
                System.out.println("文件大小: " + df.format((contentLength/1024.0/1024.0)) + "MB");
                totalLength = contentLength;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("正在下载: " + df.format((currentLength/1024.0/1024.0)) + "MB/" + df.format((totalLength/1024.0/1024.0)) + "MB, 速度: " + (length/1024) + "kb/s");
                        length = 0;
                    }
                },0,1000);
                fos = new FileOutputStream("QQ.exe");
            }

            @Override
            public void onReading(int length, byte[] buffer) throws IOException {
                fos.write(buffer,0, length);
                currentLength+=length;
                this.length+=length;
            }

            @Override
            public void onSuccess() throws IOException {
                System.out.println("下载完成");
                fos.close();
                timer.cancel();
            }

            @Override
            public void onError(String message) {
                System.out.println("下载失败");
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        fos = null;
                    }
                }
                timer.cancel();
            }

            private long currentLength;

            private long totalLength;

            private long length;

            private Timer timer = new Timer();

            private FileOutputStream fos;

        });
        while (true);
    }
}
