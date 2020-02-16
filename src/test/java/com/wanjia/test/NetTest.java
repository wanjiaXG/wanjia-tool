package com.wanjia.test;

import com.wanjiaxg.utility.FileUtility;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class NetTest {
    public static void main(String[] args) throws IOException {
        String file = "index.html";
        URLConnection urlConnection = new URL("http://www.baidu.com").openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buff = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buff)) > 0){
            fos.write(buff, 0, length);
            fos.flush();
        }
        fos.close();
        inputStream.close();

        boolean b1 = FileUtility.moveFile(file, "b1.html");
        boolean b2 = FileUtility.moveFile("b1.html", "C:\\application\\b2.html");
        System.out.println(b1);
        System.out.println(b2);
    }
}
