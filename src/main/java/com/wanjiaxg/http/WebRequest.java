package com.wanjiaxg.http;

import com.wanjiaxg.utility.IOUtility;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebRequest {

    private WebClient client;

    private Map<String, String> files;

    private Map<String, String> requestHeaders;

    private StringBuilder postContent;

    private RequestMethod method;

    private MediaType mediaType;

    private String url;

    private int retryCount;

    public WebRequest(String url, WebClient client) {
        this.client = client;
        this.retryCount = 0;
        this.requestHeaders = new HashMap<>();
        this.postContent = new StringBuilder();
        this.files = new HashMap<>();
        this.url = url;
        this.mediaType = MediaType.parse("application/x-www-form-urlencoded");
        setUserAgent(client.getUserAgent());
    }

    /**
     * 获取响应
     * @return
     */
    public Response getResponse() {
        Response response = null;
        //重试次数
        while (this.retryCount < client.getMaxRetryCount()){
            //初始化request
            Request.Builder requestBuilder = new Request.Builder().url(url);

            //添加请求头
            for(Map.Entry<String, String> entry : this.requestHeaders.entrySet()){
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
            //判断请求类型
            if(postContent.length() > 0){
                requestBuilder.post(FormBody.create(postContent.toString(), mediaType));
            } else if (this.files.size() > 0) {
                MultipartBody.Builder multipartBuilder =
                        new MultipartBody.Builder().setType(MultipartBody.FORM);

                for (Map.Entry<String, String> entry : this.files.entrySet()) {
                    multipartBuilder.addFormDataPart(
                            entry.getKey(),
                            entry.getValue(),
                            RequestBody.create(
                                    new File(entry.getValue()),
                                    MediaTypeSet.getMediaTypeByFullName(entry.getValue())
                            ));
                }
                requestBuilder.post(multipartBuilder.build());
            } else if (method == RequestMethod.HEAD) {
                requestBuilder.head();
            }

            try{
                response = client.getClient().newCall(requestBuilder.build()).execute();
                break;
            }catch (Exception e1) {
                e1.printStackTrace();
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

    /**
     * 开始连接
     * @return
     */
    public WebResponse open(){
        return new WebResponse(getResponse(), this.client);
    }

    /**
     * 异步连接获取文本响应
     * @param callback
     */
    public void openAsync(IWebResultCallback callback){
        new Thread(() -> {
            new WebResponse(getResponse(), this.client).getBody(callback);
        }).start();
    }

    /**
     * 异步连接获取流响应
     * @param callback
     */
    public void saveAsync(IWebSaveFileCallback callback){
        new Thread(() -> {
            new WebResponse(getResponse(), this.client).saveFile(callback);
        }).start();
    }

    /**
     * 添加请求头
     * @param key
     * @param value
     * @return
     */
    public WebRequest addHeader(String key, String value){
        requestHeaders.put(key.toLowerCase(), value);
        return this;
    }

    /**
     * 添加POST请求体
     * @param name
     * @param value
     * @return
     */
    public WebRequest addPostContent(String name, String value){
        this.postContent.append(name + "=" + value + "&");

        return this;
    }

    /**
     * 设置POST-json请求头
     * @param content
     * @return
     */
    public WebRequest setPostJsonContent(String content) {
        this.postContent.append(content);
        mediaType = MediaTypeSet.getMediaTypeBySuffix("json");
        return this;
    }

    /**
     * 设置POST-xml请求体
     * @param content
     * @return
     */
    public WebRequest setPostXml(String content){
        this.postContent.append(content);
        mediaType = MediaTypeSet.getMediaTypeBySuffix("xml");
        return this;
    }

    /**
     * 设置User-Agent
     * @param userAgent
     * @return
     */
    public WebRequest setUserAgent(String userAgent){
        return  addHeader("user-agent", userAgent);
    }

    /**
     * 添加上传文件
     * @param name form表单key
     * @param path 本地文件路径
     * @return
     */
    public WebRequest addFile(String name, String path){
        this.files.put(name, path);
        return this;
    }

    /**
     * 设置请求方法 仅支持POST-HEAD-GET
     * @param method
     * @return
     */
    public WebRequest setMethod(RequestMethod method) {
        this.method = method;
        return this;
    }

}
