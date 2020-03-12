package com.wanjiaxg.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IWebSaveFileCallback {
    void onReady(int stateCode, Map<String, List<String>> headerFields, long contentLength) throws IOException;
    void onReading(int length, byte[] buffer) throws IOException;
    void onSuccess() throws IOException;
    void onError(String message);
}
