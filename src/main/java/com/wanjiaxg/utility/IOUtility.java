package com.wanjiaxg.utility;

import java.io.*;

@SuppressWarnings("ALL")
public final class IOUtility {

    /**
     * 获取系统默认的字符集
     */
    public static final String DEFAULT_ENCODING = System.getProperty("sun.jnu.encoding");

    /**
     * IO流读取缓冲大小
     */
    public static final int BUFFER_SIZE = 4096;

    /**
     * 将输入流读取为字符串
     * @param is            输入流
     * @return              字符串
     * @throws IOException  读取过程可能会抛出IO异常
     */
    public static String inputStreamToString(InputStream is) throws IOException {
        return inputStreamToString(is, DEFAULT_ENCODING);
    }

    /**
     * 将输入流读取为字符串
     * @param is            输入流
     * @param encoding      字符集
     * @return              字符串
     * @throws IOException  读取过程可能会抛出IO异常
     */
    public static String inputStreamToString(InputStream is, String encoding) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, encoding);
        char[] buffer = new char[BUFFER_SIZE];
        int length;
        StringBuilder sb = new StringBuilder();
        while ((length = isr.read(buffer)) > 0){
            sb.append(buffer, 0, length);
        }
        return sb.toString();
    }

    /**
     * 复制流
     * @param source        输入流
     * @param target        输出流
     * @throws IOException  复制过程可能会抛出IO异常
     */
    public static void copyStream(InputStream source, OutputStream target) throws IOException {
        copyStream(source, target, BUFFER_SIZE);
    }

    /**
     * 复制流
     * @param source        输入流
     * @param target        输出流
     * @param bufferSize    缓冲大小
     * @throws IOException  复制过程可能会抛出IO异常
     */
    public static void copyStream(InputStream source, OutputStream target, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int length;
        while ((length = source.read(buffer)) > 0){
            target.write(buffer, 0, length);
            target.flush();
        }
    }

    /**
     * 将流数据读取为数组
     * @param stream        输入流
     * @return              字节数组
     * @throws IOException  复制过程可能会抛出IO异常
     */
    public static byte[] streamToArray(InputStream stream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = 0;
        while ((length = stream.read(buffer)) > 0){
            bos.write(buffer, 0, length);
        }
        bos.flush();
        buffer = bos.toByteArray();
        bos.close();
        return buffer;
    }

    /**
     * 关闭流
     * @param closeable     所有closeable的实现类
     */
    public static void closeStream(Closeable closeable){
        if(closeable != null) try { closeable.close(); } catch (IOException ignored) {}
    }
}
