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

@SuppressWarnings("ALL")
public final class FileUtility {

    /**
     * 获取系统默认的字符集
     */
    public static final String DEFAULT_ENCODING = System.getProperty("sun.jnu.encoding");

    /**
     * 文件跳过删除模式（包含文件夹）
     */
    public static final int CLEAN_IGNORE = 1;

    /**
     * 文件匹配删除模式（包含文件夹）
     */
    public static final int CLEAN_CONTAIN = 2;

    /**
     * 文件跳过删除模式（仅删除文件）
     */
    public static final int CLEAN_IGNORE_ONLY_FILE = 4;

    /**
     * 文件匹配删除模式（仅删除文件）
     */
    public static final int CLEAN_CONTAIN_ONLY_FILE = 8;

    /**
     * 给定一个文件的路径，创建该文件的文件夹
     * @param file      文件路径
     * @return          创建结果
     */
    public static boolean initFileDirectory(String file) {
        boolean success = true;
        String path = null;
        int index;
        //判断是否为windows系统
        if(SystemUtility.isWindows()){
            //去除路径中的盘符
            String str = file.replaceAll("^[A-Za-z]+:\\\\", "");

            //获取最后一个路径分割符的索引
            index = str.lastIndexOf("\\");

            //判断是否需要创建目录
            if(index > 0){
                //获取需要创建的文件夹路径
                path = file.substring(0, file.lastIndexOf("\\"));
            }
        }else{
            //获取最后一个路径分割符的索引
            index = file.lastIndexOf("/");

            if(index > 0){
                //获取路径
                path = file.substring(0, index);
            }
        }

        //判断是否创建文件夹
        if(path != null){
            //创建文件夹并返回结果
            success = mkdir(path);
        }

        return success;
    }

    /**
     * 创建文件夹
     * @param dir       文件夹路径
     * @return          创建结果
     */
    public static boolean mkdir(String dir){
        return mkdir(new File(dir));
    }

    /**
     * 创建文件夹
     * @param dir       文件夹对象
     * @return          创建结果
     */
    public static boolean mkdir(File dir){
        boolean success = true;
        if(dir.isFile()){
            success = false;
        }
        if(!dir.exists()){
            success = dir.mkdirs();
        }
        return success;
    }

    /**
     * 删除文件
     * @param file      文件/文件夹路径
     * @return          删除结果
     */
    public static boolean deleteFile(String file){
        return deleteFile(new File(file));
    }

    /**
     * 删除文件
     * @param file      文件/文件夹对象
     * @return          删除结果
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
     * 清空文件夹
     * @param dir       文件路径
     */
    public static void cleanDirectory(String dir){
        if(dir != null){
            cleanDirectory(new File(dir), null, CLEAN_CONTAIN);
        }
    }

    /**
     * 清空文件夹
     * @param dir       文件对象
     */
    public static void cleanDirectory(File dir) {
        cleanDirectory(dir, null, CLEAN_CONTAIN);
    }

    /**
     * 清空文件夹
     * @param dir       文件路径
     * @param regex     正则表达式
     */
    public static void cleanDirectory(String dir, String regex) {
        if(dir != null){
            cleanDirectory(new File(dir), regex, CLEAN_CONTAIN);
        }
    }

    /**
     * 清空文件夹
     * @param dir       文件对象
     * @param regex     正则表达式
     */
    public static void cleanDirectory(File dir, String regex){
        cleanDirectory(dir, regex, CLEAN_CONTAIN);
    }

    /**
     * 清空文件夹
     * @param dir       文件路径
     * @param regex     正则表达式
     * @param cleanMode 清空模式
     */
    public static void cleanDirectory(String dir, String regex, int cleanMode) {
        cleanDirectory(new File(dir), regex, cleanMode);
    }

    /**
     * 清空文件夹
     * @param dir       文件对象
     * @param regex     正则表达式
     * @param cleanMode 清空模式
     */
    public static void cleanDirectory(File dir, String regex, int cleanMode) {
        if(dir != null && dir.exists() && dir.isDirectory()){
            if(regex == null){
                regex = ".";
            }
            File[] files = dir.listFiles();
            if(files != null){
                for(File file : files){
                    String result = RegexUtility.match(file.getName(), regex);
                    if((cleanMode == CLEAN_IGNORE && result == null) ||
                       (cleanMode == CLEAN_CONTAIN && result != null)){
                        deleteFile(file);
                        //System.out.println(file.getAbsolutePath());
                    }else if((cleanMode == CLEAN_IGNORE_ONLY_FILE && result == null) ||
                             (cleanMode == CLEAN_CONTAIN_ONLY_FILE && result != null)){
                        if(file.isDirectory()){
                            cleanDirectory(file, regex, cleanMode);
                        }else {
                            deleteFile(file);
                            //System.out.println(file.getAbsolutePath());
                        }
                    }else if(file.isDirectory()){
                        cleanDirectory(file, regex, cleanMode);
                    }
                }
            }
        }
    }

    /**
     * 复制文件
     * @param source    源文件名
     * @param target    目标文件名
     * @return          复制结果
     */
    public static boolean copyFile(String source, String target){
        boolean success = false;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            File s = new File(source);
            if(FileUtility.initFileDirectory(target) && s.isFile()){
                fis = new FileInputStream(s);
                fos = new FileOutputStream(target);
                IOUtility.copyStream(fis, fos);
                success = true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            IOUtility.closeStream(fis);
            IOUtility.closeStream(fos);
        }
        return success;
    }

    /**
     * 复制文件夹
     * @param source    源文件夹名
     * @param target    目标文件夹名
     * @return          复制结果
     */
    public static boolean copyDir(String source, String target){
        File s = new File(source);
        File t = new File(target);
        boolean success = true;

        if(s.isDirectory() && mkdir(target)){
            File[] fileList = s.listFiles();
            if(fileList != null && fileList.length > 0){
                for(File file : fileList){
                    String newPath =
                            file.getAbsolutePath()
                                .replace(s.getAbsolutePath(),t.getAbsolutePath());
                    if(file.isDirectory()){
                        success = copyDir(file.getAbsolutePath(),
                                newPath
                                );
                    }else{
                        success = copyFile(file.getAbsolutePath(), newPath);
                    }
                    if(!success){
                        deleteFile(target);
                        break;
                    }
                }
            }
        }
        return success;
    }

    /**
     * 移动文件/文件夹
     * @param source    原文件名
     * @param target    目标文件
     * @return
     */
    public static boolean moveFile(String source, String target){
        initFileDirectory(target);
        File file = new File(target);
        if(file.exists()){
            deleteFile(file);
        }

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
     * @param file          读取文本所有内容
     * @return              文本内容
     */
    public static String readAllText(String file){
        return readAllText(file, DEFAULT_ENCODING);
    }

    /**
     * 读取文件内容为字符串
     * @param file      文本内容
     * @param encoding  字符集
     * @return          文本内容
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
     * @param content   文本内容
     * @param file      文件路径
     * @return          写入结果
     */
    public static boolean writeAllText(String content, String file){
        return writeAllText(content, file, DEFAULT_ENCODING);
    }

    /**
     * 将字符串写入到文件
     * @param content   文本内容
     * @param file      文件路径
     * @param encoding  字符集
     * @return          写入结果
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
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtility.closeStream(fos);
        }
        return success;
    }

    /**
     * 获取文件夹与其子目录下的所有文件
     * @param dir       文件夹路径
     * @return          返回结果集
     */
    public static List<File> getFilesByDir(String dir){
        return getFilesByDir(new File(dir));
    }

    /**
     * 获取文件夹与其子目录下的所有文件
     * @param dir       文件夹对象
     * @return          返回结果集
     */
    public static List<File> getFilesByDir(File dir){
        List<File> list = new ArrayList<>();
        if(dir != null && dir.isDirectory()){
            File[] files = dir.listFiles();
            if(files != null){
                for (File file : files){
                    if(file.isDirectory()){
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
