package com.wanjia.test;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.zip.Zip;

import java.io.File;
import java.util.List;

public class FileTest {
    public static void main(String[] args) {
        Zip zip = new Zip();
        boolean make = zip.make("C:\\application", "C:\\application\\a.zip");
        System.out.println(make);
    }
}
