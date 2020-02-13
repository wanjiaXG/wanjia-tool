package com.wanjiaxg.http;

import java.util.List;
import java.util.Map;

public interface IWebResultCallback {
    void onReady(int stateCode, Map<String, List<String>> headerFields);
    void onSuccess(String result);
    void onError(String message);
}
