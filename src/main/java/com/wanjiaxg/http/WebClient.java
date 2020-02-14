package com.wanjiaxg.http;

import com.wanjiaxg.utility.ReflectionUtility;
import okhttp3.CookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import javax.activation.MimeType;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WebClient {

    private OkHttpClient client;

    private int connectTimeoutMillis = 30000;

    private int writeTimeoutMillis = 30000;

    private int readTimeoutMillis = 30000;

    private int maxRetryCount = 5;

    private int retryIntervalMillis = 1000;

    private int bufferSize = 1024;

    private String encoding = "UTF-8";

    private String userAgent = "WebClient(Java) by wanjia 1.0";

    private String tmpFileSuffix = ".wanjia";

    private final IWebResultCallback webResultCallback = new IWebResultCallback() {
        @Override
        public void onReady(int stateCode, Map<String, List<String>> headerFields) {

        }

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onError(String message) {

        }
    };

    private final IWebSaveFileCallback webSaveFileCallback = new IWebSaveFileCallback() {
        @Override
        public void onReady(int stateCode, Map<String, List<String>> headerFields, long contentLength) {

        }

        @Override
        public void onReading(int length) {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(String message) {

        }
    };

    /**
     * 跳过SSL证书验证
     */
    private static SSLContext ssl;
    private static HostnameVerifier verifier;
    static{
        try {
            verifier = (ssl, sslSession) -> true;
            ssl = SSLContext.getInstance("SSL");
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){}
                    }
            };
            ssl.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WebClient webClient;

    public WebClient(){
        initClient();
    }

    public static WebClient getInstance(){
        if(webClient == null){
            webClient = new WebClient();
            webClient.setCookieJar(WebCookieJar.getInstance());
        }
        return webClient;
    }

    @SuppressWarnings("deprecation")
    private void initClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(this.connectTimeoutMillis, TimeUnit.MILLISECONDS)
                .readTimeout(this.readTimeoutMillis, TimeUnit.MILLISECONDS)
                .writeTimeout(this.writeTimeoutMillis, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .sslSocketFactory(ssl.getSocketFactory())
                .hostnameVerifier(verifier);
        client = builder.build();
    }

    public WebRequest load(String url){
        return new WebRequest(url, this);
    }

    public OkHttpClient getClient() {
        return client;
    }

    public int getConnectTimeoutMillis() {
        return this.client.connectTimeoutMillis();
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        ReflectionUtility.setValue(client, "connectTimeoutMillis", connectTimeoutMillis);
    }

    public int getWriteTimeoutMillis() {
        return this.client.writeTimeoutMillis();
    }

    public void setWriteTimeoutMillis(int writeTimeoutMillis) {
        ReflectionUtility.setValue(client, "writeTimeoutMillis", writeTimeoutMillis);
    }

    public int getReadTimeoutMillis() {
        return this.client.readTimeoutMillis();
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        ReflectionUtility.setValue(client, "readTimeoutMillis", readTimeoutMillis);
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public int getRetryIntervalMillis() {
        return retryIntervalMillis;
    }

    public void setRetryIntervalMillis(int retryIntervalMillis) {
        this.retryIntervalMillis = retryIntervalMillis;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getTmpFileSuffix() {
        return tmpFileSuffix;
    }

    public void setTmpFileSuffix(String tmpFileSuffix) {
        this.tmpFileSuffix = tmpFileSuffix;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setCookieJar(CookieJar cookieJar){
        ReflectionUtility.setValue(this.client,"cookieJar",cookieJar);
    }

    IWebResultCallback getWebResultCallback() {
        return webResultCallback;
    }

    IWebSaveFileCallback getWebSaveFileCallback() {
        return webSaveFileCallback;
    }

}
