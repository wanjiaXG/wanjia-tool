package com.wanjiaxg.utility;

import com.wanjiaxg.http.WebClient;
import com.wanjiaxg.http.IWebResultCallback;
import com.wanjiaxg.http.IWebSaveFileCallback;

import java.io.*;

public final class IOUtility {

    private static WebClient webClient;

    static {
        webClient = WebClient.getInstance();
    }

    private static String encoding = "UTF-8";

    private static int bufferSize = 1024;

    public static String readAllText(String file){
        return readAllText(file, encoding);
    }

    public static String readAllText(String file, String encoding){
        String result = null;
        try {
            result = inputStreamToString(new FileInputStream(file), encoding);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean writeAllText(String content, String file){
        return writeAllText(content, file, encoding);
    }

    public static boolean writeAllText(String content, String file, String encoding){
        boolean success = false;
        FileOutputStream fos = null;
        try {
            if(initFileDirectory(file)){
                byte[] bytes = content.getBytes(encoding);
                fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeStream(fos);
        }
        return success;
    }

    public static boolean initFileDirectory(String file) {
        boolean success = true;
        String path = "";
        int index = -1;
        //根据文件路径分隔符判断是否为windows系统
        if(String.valueOf(File.separatorChar).equals("\\")){
            //Windows系统分析
            String str = file.replaceAll("^[A-Za-z]+:\\\\", ""); // abc\hello.mp3
            index = str.lastIndexOf("\\");
            if(index > 0){
                path = file.substring(0, file.lastIndexOf("\\"));
            }else {
                index = file.indexOf("\\");
                if(index == -1){
                    path = ".\\";
                }else {
                    path = file.substring(0, ++index);
                    index = -1;
                }
            }
        }else {
            //Linux系统分析
            index = file.lastIndexOf("/");
            if(index > 0){
                path = file.substring(0, index);
            }else if(index == 0){
                path = "/";
                index = -1;
            }else{
                path = "./";
            }
        }

        if(index != -1){
            success = mkdir(path);
        }
        return success;
    }

    public static void closeStream(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static boolean mkdir(String dir){
        return mkdir(new File(dir));
    }

    public static boolean mkdir(File dir){
        boolean success = true;
        if(!dir.exists()){
            success = dir.mkdirs();
        }
        return success;
    }

    public static boolean moveFile(String source, String target){
        return new File(source).renameTo(new File(target));
    }

    public static boolean deleteFile(String file){
        return new File(file).delete();
    }

    public static String inputStreamToString(InputStream is){
        return inputStreamToString(is, encoding);
    }

    public static String inputStreamToString(InputStream is, String encoding){
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
            closeStream(is);
        }
        return result;
    }

    public static boolean copyFile(String source, String target){
        boolean success = false;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if(initFileDirectory(target)){
                fis = new FileInputStream(source);
                fos = new FileOutputStream(target);
                byte[] buffer = new byte[bufferSize];
                int length = 0;
                while ((length = fis.read(buffer)) > 0){
                    fos.write(buffer, 0, length);
                    fos.flush();
                }
                success = true;
            }else {
                throw new Exception("Can't make directory " + target);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeStream(fis);
            closeStream(fos);
        }
        return success;
    }

    public static String downloadString(String url){
        return webClient.load(url).open().body();
    }

    public static boolean downloadFile(String url, String file){
        return webClient.load(url).open().save(file);
    }

    public static void downloadStringAsync(String url, IWebResultCallback callback){
        webClient.load(url).openAsync(callback);
    }

    public static void downloadFileAsync(String url, String file, IWebSaveFileCallback callback){
        webClient.load(url).openAsync(file, callback);
    }

}
