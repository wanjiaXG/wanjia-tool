package com.wanjiaxg.utility;

import com.wanjiaxg.zip.Zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FileUtility {

    public static String encoding = "UTF-8";

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

    public static boolean deleteFile(String file){
        return deleteFile(new File(file));
    }

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

    public static boolean copyFile(String source, String target){
        boolean success = false;
        try {
            if(FileUtility.initFileDirectory(target)){
                success = IOUtility.copyStream(
                        new FileInputStream(source),
                        new FileOutputStream(target));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return success;
    }

    public static boolean moveFile(String source, String target){
        boolean success = false;
        File file = new File(target);
        if(file.exists()) deleteFile(file);
        success = new File(source).renameTo(file);
        if(!success){
            try {
                Path s = FileSystems.getDefault().getPath(source);
                Path t = FileSystems.getDefault().getPath(target);
                Files.move(s, t);
                success = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return success;
    }

    public static String readAllText(String file){
        return readAllText(file, encoding);
    }

    public static String readAllText(String file, String encoding){
        String result = null;
        try { result = IOUtility.inputStreamToString(new FileInputStream(file), encoding); } catch (Exception ignored) {}
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
            IOUtility.closeStream(fos);
        }
        return success;
    }

    public static boolean unzip(String file){
        return unzip(file, RegexUtility.getFilePath(file));
    }

    public static boolean unzip(String file, String path){
        return unzip(file, path, encoding);
    }

    public static boolean unzip(String file, String path, String encoding){
        return unzip(file, path, encoding, false);
    }

    public static boolean unzip(String file, String path, String encoding, boolean autoCreateDirectory){
        return new Zip().setAutoCreateDirectory(autoCreateDirectory)
                .setEncoding(encoding)
                .unpack(file, path);

    }

    public static boolean makeZip(String dir, String file){
        return new Zip().make(dir, file);
    }

    public static void cleanDirectory(String dir) {
        if(dir != null){
            cleanDirectory(dir, null);
        }
    }

    public static void cleanDirectory(File dir) {
        cleanDirectory(dir, null);
    }

    /**
     * 删除正则表达式匹配项的文件以及文件夹
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
                        System.out.println(file);
                        deleteFile(file);
                    }else if(file.isDirectory()){
                        cleanDirectory(file, filterRegex);
                    }
                }
            }
        }
    }

    public static void cleanDirectory(String dir, String filterRegex) {
        if(dir != null){
            cleanDirectory(new File(dir), filterRegex);
        }
    }

    public static List<File> getFilesByDir(String dir){
        return getFilesByDir(new File(dir));
    }

    public static List<File> getFilesByDir(File dir){
        List<File> list = new ArrayList<>();
        if(dir != null && dir.isDirectory()){
            File[] files = dir.listFiles();
            for (File file : files){
                if(file.isDirectory()){
                    list.add(file);
                    list.addAll(getFilesByDir(file));
                }else {
                    list.add(file);
                }
            }
        }
        return list;
    }

}
