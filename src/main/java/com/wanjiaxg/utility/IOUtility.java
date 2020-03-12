package com.wanjiaxg.utility;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class IOUtility {

    public static String encoding = System.getProperty("sun.jnu.encoding");

    public static int bufferSize = 4096;

    public static String inputStreamToString(InputStream is){
        return inputStreamToString(is, encoding, true);
    }

    public static String inputStreamToString(InputStream is, String encoding){
        return inputStreamToString(is, encoding, true);
    }

    public static String inputStreamToString(InputStream is, boolean autoCloseStream){
        return inputStreamToString(is, encoding, autoCloseStream);
    }

    public static String inputStreamToString(InputStream is, String encoding, boolean autoCloseStream){
        String result = null;
        try {
            InputStreamReader isr = new InputStreamReader(is, encoding);
            char[] buffer = new char[bufferSize];
            int length = 0;
            StringBuilder sb = new StringBuilder();
            while ((length = isr.read(buffer)) > 0){
                sb.append(buffer, 0, length);
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(autoCloseStream) closeStream(is);
        }
        return result;
    }

    public static boolean copyStream(InputStream source, OutputStream target){
        return copyStream(source, target, bufferSize, true);
    }

    public static boolean copyStream(InputStream source, OutputStream target, int bufferSize){
        return copyStream(source, target, bufferSize, true);
    }

    public static boolean copyStream(InputStream source, OutputStream target, boolean autoCloseStream){
        return copyStream(source, target, bufferSize, autoCloseStream);
    }

    public static boolean copyStream(InputStream source, OutputStream target, int bufferSize,  boolean autoCloseStream){
        boolean success = false;
        try{
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = source.read(buffer)) > 0){
                target.write(buffer, 0, length);
                target.flush();
            }
            success = true;
        }catch (Exception e){

        }finally {
            if(autoCloseStream){
                closeStream(source);
                closeStream(target);
            }
        }
        return success;
    }

    public static byte[] streamToArray(InputStream stream){
        return streamToArray(stream, true);
    }

    public static byte[] streamToArray(InputStream stream, boolean autoClose){
        ByteArrayOutputStream bos = null;
        byte[] result = null;
        try {
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = stream.read(buffer)) > 0){
                bos.write(buffer, 0, length);
            }
            result = bos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(autoClose) closeStream(stream);
            closeStream(bos);
        }
        return result;
    }

    public static void closeStream(Closeable closeable){
        if(closeable != null) try { closeable.close(); } catch (IOException ignored) {}
    }
}
