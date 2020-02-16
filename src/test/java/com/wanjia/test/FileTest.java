package com.wanjia.test;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.IOUtility;
import com.wanjiaxg.zip.Zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class FileTest {
    public static void main(String[] args) throws FileNotFoundException {
        FileUtility.moveFile("C:\\a\\b.txt","C:\\a\\c.txt");
    }
}
