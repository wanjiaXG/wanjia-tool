package com.wanjiaxg.http;

import java.util.List;
import java.util.Map;

public interface IWebSaveFileCallback {
    void onReady(int stateCode, Map<String, List<String>> headerFields, long contentLength);
    void onReading(int length);
    void onSuccess();
    void onError(String message);
}
