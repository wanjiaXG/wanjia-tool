package com.wanjiaxg.utility;

import com.wanjiaxg.exception.FileCantCreateException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtility {

    private ZipUtility(){}

    private static String encoding = System.getProperty("sun.jnu.encoding");

    private static int bufferSize = 4096;

    /**
     * 解压文件
     * @param is zip文件原始IO流
     * @param path 解压路径
     * @throws IOException
     */
    public static void unpack(InputStream is, String path) throws IOException {
        unpack(is, path, encoding);
    }

    /**
     * 解压文件
     * @param is zip文件原始IO流
     * @param path 解压路径
     * @param encoding zip文件字符集
     * @throws IOException
     */
    public static void unpack(InputStream is, String path, String encoding) throws IOException {
        ZipInputStream zis = new ZipInputStream(is, Charset.forName(encoding));
        ZipEntry entry = null;
        byte[] buffer = new byte[bufferSize];
        int length = 0;
        while ((entry = zis.getNextEntry()) != null){
            String fileName = path + File.separatorChar + entry.getName();
            File file = new File(fileName);
            if(entry.isDirectory()){
                if(!file.exists() && !file.mkdirs()){
                    throw new FileCantCreateException(fileName);
                }
            }else{
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                        fos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    /**
     * 将指定文件夹打包为zip
     * @param dir 文件夹路径
     * @param os zip文件输出流
     * @throws IOException
     */
    public static void make(String dir, OutputStream os) throws IOException {
        make(dir, os, encoding);
    }

    /**
     * 将指定文件夹打包为zip
     * @param dir 文件夹路径
     * @param os zip文件输出流
     * @param encoding zip文件字符集
     * @throws IOException
     */
    public static void make(String dir, OutputStream os, String encoding) throws IOException {
        List<File> files = FileUtility.getFilesByDir(dir);
        ZipOutputStream zos = new ZipOutputStream(os, Charset.forName(encoding));
        for (File item : files){
            String entryName = item.getAbsolutePath().replace(dir,"");
            if(item.isDirectory()){
                writeDirectory(zos, entryName);
            }else{
                writeFile(zos, entryName, item);
            }
        }
        zos.finish();
    }

    /**
     * 将map中的文件关系打包成zip
     * @param fileMap map结构 key: zip内文件路径, value: 文件对象
     * @param os zip文件输出流
     * @throws IOException
     */
    public static void  make(Map<String, File> fileMap, OutputStream os) throws IOException {
        make(fileMap, os, encoding);
    }

    /**
     * 将map中的文件关系打包成zip
     * @param fileMap map结构 key: zip内文件路径, value: 文件对象
     * @param os zip文件输出流
     * @param encoding zip文件字符集
     * @throws IOException
     */
    public static void  make(Map<String, File> fileMap, OutputStream os, String encoding) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os, Charset.forName(encoding));
        for(Map.Entry<String, File> entry : fileMap.entrySet()){
            if(entry.getValue().isDirectory()){
                writeDirectory(zos, entry.getKey());
            }else {
                writeFile(zos, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 写入文件夹到zip流中
     * @param zos zip文件输出流
     * @param entryName zip内文件夹路径
     * @throws IOException
     */
    public static void writeDirectory(ZipOutputStream zos, String entryName) throws IOException {
        writeFile(zos, entryName.endsWith("/") ? entryName : entryName + "/", null);
    }

    /**
     * 写入文件到zip流中
     * @param zos zip文件输出流
     * @param entryName zip内文件路径
     * @param file 文件对象
     * @throws IOException
     */
    public static void writeFile(ZipOutputStream zos, String entryName, File file) throws IOException {
        if(entryName.startsWith(File.separator)){
            entryName = entryName.substring(1);
        }
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        if(file != null){
            try(FileInputStream fis = new FileInputStream(file)){
                byte[] buffer = new byte[bufferSize];
                int length = 0;
                while ((length = fis.read(buffer)) > 0){
                    zos.write(buffer,0,length);
                    zos.flush();
                }
            }catch (IOException e){
                throw e;
            }
        }
        zos.closeEntry();
    }

}
