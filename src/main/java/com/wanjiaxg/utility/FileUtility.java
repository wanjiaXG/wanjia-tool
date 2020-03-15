package com.wanjiaxg.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FileUtility {

    public static String encoding = System.getProperty("sun.jnu.encoding");

    /**
     * 给定一个文件绝对路径，创建该文件的文件夹
     * @param file
     * @return
     */
    public static boolean initFileDirectory(String file) {
        boolean success = true;
        String path = "";
        int index = -1;
        //根据文件路径分隔符判断是否为windows系统
        if(String.valueOf(File.separatorChar).equals("\\")){
            //Windows系统分析
            String str = file.replaceAll("^[A-Za-z]+:\\\\", "");
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

    /**
     * 创建文件夹
     * @param dir
     * @return
     */
    public static boolean mkdir(String dir){
        return mkdir(new File(dir));
    }

    /**
     * 创建文件夹
     * @param dir
     * @return
     */
    public static boolean mkdir(File dir){
        boolean success = true;
        if(!dir.exists()){
            success = dir.mkdirs();
        }
        return success;
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean deleteFile(String file){
        return deleteFile(new File(file));
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean deleteFile(File file){
        boolean success = false;
        try{
            if(file.isDirectory()){
                cleanDirectory(file);
            }
            success = file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 复制文件
     * @param source
     * @param target
     * @return
     */
    public static boolean copyFile(String source, String target){
        boolean success = false;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if(FileUtility.initFileDirectory(target)){
                fis = new FileInputStream(source);
                fos = new FileOutputStream(target);
                IOUtility.copyStream(fis,fos);
            }
            success = true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            IOUtility.closeStream(fis);
            IOUtility.closeStream(fos);
        }
        return success;
    }

    /**
     * 移动文件
     * @param source
     * @param target
     * @return
     */
    public static boolean moveFile(String source, String target){
        initFileDirectory(target);
        File file = new File(target);
        if(file.exists()) deleteFile(file);
        boolean success = new File(source).renameTo(file);
        if(!success){
            try {
                Path s = FileSystems.getDefault().getPath(source);
                Path t = FileSystems.getDefault().getPath(target);
                Files.move(s, t);
                success = true;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * 读取文件内容为字符串
     * @param file
     * @return
     */
    public static String readAllText(String file){
        return readAllText(file, encoding);
    }

    /**
     * 读取文件内容为字符串
     * @param file
     * @param encoding
     * @return
     */
    public static String readAllText(String file, String encoding){
        String result = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            result = IOUtility.inputStreamToString(fis, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtility.closeStream(fis);
        }
        return result;
    }

    /**
     * 将字符串写入到文件
     * @param content
     * @param file
     * @return
     */
    public static boolean writeAllText(String content, String file){
        return writeAllText(content, file, encoding);
    }

    /**
     * 将字符串写入到文件
     * @param content
     * @param file
     * @param encoding
     * @return
     */
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
            IOUtility.closeStream(fos);
        }
        return success;
    }

    /**
     * 清空文件夹
     * @param dir
     */
    public static void cleanDirectory(File dir) {
        cleanDirectory(dir, null);
    }

    /**
     * 清空文件夹
     * @param dir
     * @param filterRegex
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void cleanDirectory(File dir, String filterRegex) {
        if(dir != null && dir.exists() && dir.isDirectory()){
            if(filterRegex == null){
                filterRegex = ".";
            }
            File[] files = dir.listFiles();
            if(files != null){
                for(File file : files){
                    if(RegexUtility.getFirstResult(file.getName(), filterRegex) != null){
                        if(file.isDirectory()){
                            cleanDirectory(file);
                        }
                        deleteFile(file);
                    }else if(file.isDirectory()){
                        cleanDirectory(file, filterRegex);
                    }
                }
            }
        }
    }

    /**
     * 清空文件夹
     * @param dir
     * @param filterRegex
     */
    public static void cleanDirectory(String dir, String filterRegex) {
        if(dir != null){
            cleanDirectory(new File(dir), filterRegex);
        }
    }

    /**
     * 获取文件夹下边所有文件
     * @param dir
     * @return
     */
    public static List<File> getFilesByDir(String dir){
        return getFilesByDir(new File(dir));
    }

    /**
     * 获取文件夹下边所有文件
     * @param dir
     * @return
     */
    public static List<File> getFilesByDir(File dir){
        List<File> list = new ArrayList<>();
        if(dir != null && dir.isDirectory()){
            File[] files = dir.listFiles();
            if(files != null){
                for (File file : files){
                    if(file.isDirectory()){
                        list.add(file);
                        list.addAll(getFilesByDir(file));
                    }else {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

}
