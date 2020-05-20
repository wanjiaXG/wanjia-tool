package com.wanjiaxg.http;

import okhttp3.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WebRequest {

    /**
     * 客户端引用
     */
    private WebClient client;

    /**
     * 上传的文件集合
     */
    private Map<String, String> files;

    /**
     * HTTP请求头集合
     */
    private Map<String, String> requestHeaders;

    /**
     * POST请求体
     */
    private StringBuilder postContent;

    /**
     * 请求内容的类型
     */
    private MediaType mediaType;

    /**
     * 请求的URL
     */
    private String url;

    /**
     * 失败重试的次数
     */
    private int retryCount;

    /**
     * 构造方法
     * @param url       请求的URL
     * @param client    客户端对象
     */
    WebRequest(String url, WebClient client) {
        this.client = client;
        this.retryCount = client.getMaxRetryCount();
        this.requestHeaders = new HashMap<>();
        this.files = new HashMap<>();
        this.url = url;
        setUserAgent(client.getUserAgent());
    }

    /**
     * 获取响应
     * @return  响应对象
     */
    Response getResponse() {
        Response response = null;
        //初始化request
        Request.Builder requestBuilder = new Request.Builder().url(url);

        //添加请求头
        for(Map.Entry<String, String> entry : this.requestHeaders.entrySet()){
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        if(postContent != null) {
            requestBuilder.post(FormBody.create(postContent.toString(), mediaType));
        }else if(files.size() > 0){
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
        }

        try{
            response = client.getClient().newCall(requestBuilder.build()).execute();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 开始连接
     * @return
     */
    public WebResponse open(){
        Response response = null;
        for(int i = 0; i < retryCount; i++){
            response = getResponse();
            if(response != null){
                break;
            }
            try {
                Thread.sleep(client.getRetryIntervalMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new WebResponse(response, this.client);
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryCount() {
        return retryCount;
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
        this.postContent
                .append(name)
                .append("=")
                .append(value)
                .append("&");
        mediaType = MediaType.parse("application/x-www-form-urlencoded");
        return this;
    }

    /**
     * 设置POST-json请求头
     * @param content
     * @return
     */
    public WebRequest setPostJsonContent(String content) {
        this.postContent = new StringBuilder(content);
        mediaType = MediaTypeSet.getMediaTypeBySuffix("json");
        return this;
    }

    /**
     * 设置POST-xml请求体
     * @param content
     * @return
     */
    public WebRequest setPostXml(String content){
        this.postContent = new StringBuilder(content);
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

}
