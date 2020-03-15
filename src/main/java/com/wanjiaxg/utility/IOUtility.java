package com.wanjiaxg.utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class IOUtility {

    public static String encoding = System.getProperty("sun.jnu.encoding");

    public static int bufferSize = 4096;

    public static String inputStreamToString(InputStream is) throws IOException {
        return inputStreamToString(is, encoding);
    }

    public static String inputStreamToString(InputStream is, String encoding) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, encoding);
        char[] buffer = new char[bufferSize];
        int length = 0;
        StringBuilder sb = new StringBuilder();
        while ((length = isr.read(buffer)) > 0){
            sb.append(buffer, 0, length);
        }
        return sb.toString();
    }

    public static void copyStream(InputStream source, OutputStream target) throws IOException {
        copyStream(source, target, bufferSize);
    }

    public static void copyStream(InputStream source, OutputStream target, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int length = 0;
        while ((length = source.read(buffer)) > 0){
            target.write(buffer, 0, length);
            target.flush();
        }
    }

    public static byte[] streamToArray(InputStream stream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int length = 0;
        while ((length = stream.read(buffer)) > 0){
            bos.write(buffer, 0, length);
        }
        bos.flush();
        buffer = bos.toByteArray();
        bos.close();
        return buffer;
    }

    public static void closeStream(Closeable closeable){
        if(closeable != null) try { closeable.close(); } catch (IOException ignored) {}
    }
}
