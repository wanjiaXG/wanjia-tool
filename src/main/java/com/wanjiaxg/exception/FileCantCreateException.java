package com.wanjiaxg.exception;

import java.io.IOException;

public class FileCantCreateException extends IOException {

    public FileCantCreateException() {
    }

    public FileCantCreateException(String message) {
        super("无法创建文件夹: " + message);
    }
}
