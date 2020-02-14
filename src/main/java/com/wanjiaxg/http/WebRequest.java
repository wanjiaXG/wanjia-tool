package com.wanjiaxg.http;

import com.wanjiaxg.utility.IOUtility;
import okhttp3.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WebRequest {

    private WebClient client;

    private Map<String, String> files;

    private Map<String, String> requestHeaders;

    private Map<String, String> postContent;

    private String postJson;

    private RequestMethod method;

    private String url;

    private int retryCount;

    public WebRequest(String url, WebClient client) {
        this.client = client;
        this.retryCount = 0;
        this.requestHeaders = new HashMap<>();
        this.files = new HashMap<>();
        this.url = url;
        setUserAgent(client.getUserAgent());
    }

    public Response getResponse(){
        Response response = null;
        while (this.retryCount < client.getMaxRetryCount()){
            try{
                Request.Builder requestBuilder = new Request.Builder().url(url);
                for(Map.Entry<String, String> entry : this.requestHeaders.entrySet()){
                    requestBuilder.addHeader(entry.getKey(), entry.getValue());
                }
                if(postJson != null){
                    requestBuilder.post(FormBody.create(postJson, MediaTypeSet.getMediaTypeBySuffix("json")));
                }
                else if(postContent != null){
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    for(Map.Entry<String, String> entry : this.postContent.entrySet()){
                        formBuilder.add(entry.getKey(), entry.getValue());
                    }
                    requestBuilder.post(formBuilder.build());
                }else if(this.files.size() > 0){
                    MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM);
                    for(Map.Entry<String, String> entry : this.files.entrySet()){
                        multipartBuilder.addFormDataPart(
                                entry.getKey(),
                                entry.getValue(),
                                RequestBody.create(new File(entry.getValue()), MediaTypeSet.getMediaTypeByFullName(entry.getValue())));
                    }
                    requestBuilder.post(multipartBuilder.build());
                }else if(method == RequestMethod.HEAD){
                    requestBuilder.head();
                }

                response = client.getClient().newCall(requestBuilder.build()).execute();
                int code = response.code();
                if(code >=100){
                    break;
                }else {
                    IOUtility.closeStream(response);
                    response = null;
                }
            }catch (Exception e1){
                try {
                    Thread.sleep(this.client.getRetryIntervalMillis());
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            this.retryCount++;
        }
        return response;
    }

    public WebResponse open(){
        return new WebResponse(getResponse(), this.client);
    }

    public void openAsync(IWebResultCallback callback){
        new Thread(() -> {
            new WebResponse(getResponse(), this.client).body(callback);
        }).start();
    }

    public void openAsync(String file, IWebSaveFileCallback callback){
        new Thread(() -> {
            new WebResponse(getResponse(), this.client).save(file, callback);
        }).start();
    }

    public WebRequest addHeader(String key, String value){
        requestHeaders.put(key.toLowerCase(), value);
        return this;
    }

    public WebRequest addPost(String name, String value){
        if(name != null & value != null && !name.trim().equals("")){
            if(postContent == null) postContent = new HashMap<>();
            postContent.put(name, value);
        }
        return this;
    }

    public String getPostJson() {
        return postJson;
    }

    public WebRequest setPostJson(String postJson) {
        this.postJson = postJson;
        return this;
    }

    public WebRequest setUserAgent(String userAgent){
        return  addHeader("user-agent", userAgent);
    }

    public WebRequest addFile(String name, String path){
        if(name != null &&
            path != null &&
            !"".equals(name.trim()) &&
            !"".equals(path.trim())){

            this.files.put(name, path);
        }
        return this;
    }

    public String getMethod() {
        return method.name();
    }

    public WebRequest setMethod(RequestMethod method) {
        this.method = method;
        return this;
    }
}
