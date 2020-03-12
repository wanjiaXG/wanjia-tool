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

    private int maxRetryCount = 5;

    private int retryIntervalMillis = 1000;

    private int bufferSize = 4096;

    private String encoding = "UTF-8";

    private String userAgent = "WebClient(Java) by wanjia 1.0";

    /**
     * 跳过SSL证书验证
     */
    private static SSLContext ssl;
    private static HostnameVerifier verifier;
    private static TrustManager[] trustAllCerts;
    static{
        try {
            verifier = (ssl, sslSession) -> true;
            ssl = SSLContext.getInstance("SSL");
            trustAllCerts = new TrustManager[]{
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

    private void initClient(){
        int readTimeoutMillis = 30000;
        int writeTimeoutMillis = 30000;
        int connectTimeoutMillis = 30000;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutMillis, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeoutMillis, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .sslSocketFactory(ssl.getSocketFactory(),(X509TrustManager)trustAllCerts[0])
                .hostnameVerifier(verifier);
        client = builder.build();
    }

    public WebRequest load(String url){
        return new WebRequest(url, this);
    }

    protected OkHttpClient getClient() {
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

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setCookieJar(CookieJar cookieJar){
        ReflectionUtility.setValue(this.client,"cookieJar", cookieJar);
    }

}
