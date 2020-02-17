package com.wanjia.test;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.IOUtility;
import com.wanjiaxg.zip.Zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class FileTest {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) throws FileNotFoundException {
        String path = "C:\\b";
        File file = new File(path);
        FileUtility.cleanDirectory(file,"wanjia");
    }
}
