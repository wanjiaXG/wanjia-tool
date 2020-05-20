package com.wanjiaxg.utility;

import java.io.File;

@SuppressWarnings("ALL")
public final class SystemUtility {

    /**
     * 判断是否为window系统
     * @return 返回结果
     */
    public static boolean isWindows(){
        return File.separatorChar == 92; //92为字符'\' windows的文件分割符为\
    }
}
