package com.wanjiaxg.zip;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.IOUtility;
import com.wanjiaxg.utility.RegexUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip {

    private String encoding = "GBK";

    private boolean autoCreateDirectory;

    public String getEncoding() {
        return encoding;
    }

    public Zip setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public boolean isAutoCreateDirectory() {
        return autoCreateDirectory;
    }

    public Zip setAutoCreateDirectory(boolean autoCreateDirectory) {
        this.autoCreateDirectory = autoCreateDirectory;
        return this;
    }

    public boolean unpack(String file, String path){
        return unpack(new File(file), path);
    }

    public boolean unpack(File file, String path){
        boolean success = false;
        if(file.exists()) {
            String baseDir = path
                    + (autoCreateDirectory ? File.separatorChar + RegexUtility.getFileNameIgnoreSuffix(file.getName()) : "");
            File dir = new File(baseDir);
            dir.mkdirs();
            if(dir.canWrite()){
                ZipInputStream zis = null;
                try {
                    zis = new ZipInputStream(new FileInputStream(file), Charset.forName(encoding));
                    ZipEntry entry = null;
                    while ((entry = zis.getNextEntry()) != null){
                        String fileName = baseDir + File.separatorChar + entry.getName();
                        if(entry.isDirectory()){
                            FileUtility.mkdir(fileName);
                        }else {
                            IOUtility.copyStream(zis, new FileOutputStream(fileName), false);
                        }
                    }
                    success = true;
                }catch (Exception e){
                    if(autoCreateDirectory){
                        dir.delete();
                    }else {
                        FileUtility.cleanDirectory(dir);
                    }
                }finally {
                    IOUtility.closeStream(zis);
                }
            }
        }
        return success;
    }

    public boolean make(String dir, String file){
        boolean success = false;
        ZipOutputStream zos = null;
        try{
            if(!dir.endsWith(File.separator)){
                dir += File.separator;
            }
            List<File> list = FileUtility.getFilesByDir(dir);
            zos = new ZipOutputStream(new FileOutputStream(file), Charset.forName(encoding));
            for (File item : list){
                if(item.canRead() && !file.endsWith(item.getName())){
                    if(item.isDirectory()){
                        ZipEntry entry = new ZipEntry(item.getAbsolutePath().replace(dir,"") + File.separator);
                        zos.putNextEntry(entry);
                    }else{
                        ZipEntry entry = new ZipEntry(item.getAbsolutePath().replace(dir,""));
                        zos.putNextEntry(entry);
                        FileInputStream fis = new FileInputStream(item.getAbsolutePath());
                        IOUtility.copyStream(fis , zos, false);
                        IOUtility.closeStream(fis);
                    }
                    zos.closeEntry();
                }
            }
            success = true;
        }catch (Exception e){
            FileUtility.deleteFile(file);
            e.printStackTrace();
        }finally {
            IOUtility.closeStream(zos);
        }
        return success;
    }
}
