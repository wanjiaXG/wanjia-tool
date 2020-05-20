package com.wanjiaxg.http;

import com.wanjiaxg.utility.ReflectionUtility;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ALL")
public class WebClient {

    private OkHttpClient client;

    /**
     * 请求失败后重试的次数
     */
    private int maxRetryCount = 5;

    /**
     * 失败重试的间隔，单位ms
     */
    private int retryIntervalMillis = 1000;

    /**
     * 数据读取的缓冲大小
     */
    private int bufferSize = 4096;

    /**
     * 设置字符集
     */
    private String encoding = "UTF-8";

    /**
     * 设置User-Agent
     */
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

    public WebClient(){
        initClient();
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

    /**
     * 获取客户端
     * @return
     */
    public OkHttpClient getClient() {
        return client;
    }

    /**
     * 初始化一个连接
     * @param url
     * @return
     */
    public WebRequest load(String url){
        return new WebRequest(url, this);
    }

    /**
     * 获取连接超时时间
     * @return
     */
    public int getConnectTimeoutMillis() {
        return this.client.connectTimeoutMillis();
    }

    /**
     * 设置连接超时时间
     * @param connectTimeoutMillis
     */
    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        ReflectionUtility.setValue(client, "connectTimeoutMillis", connectTimeoutMillis);
    }

    /**
     * 获取写入超时时间
     * @return
     */
    public int getWriteTimeoutMillis() {
        return this.client.writeTimeoutMillis();
    }

    /**
     * 设置写入超时时间
     * @param writeTimeoutMillis
     */
    public void setWriteTimeoutMillis(int writeTimeoutMillis) {
        ReflectionUtility.setValue(client, "writeTimeoutMillis", writeTimeoutMillis);
    }

    /**
     * 获取读取超时时间
     * @return
     */
    public int getReadTimeoutMillis() {
        return this.client.readTimeoutMillis();
    }

    /**
     * 设置读取超时时间
     * @param readTimeoutMillis
     */
    public void setReadTimeoutMillis(int readTimeoutMillis) {
        ReflectionUtility.setValue(client, "readTimeoutMillis", readTimeoutMillis);
    }

    /**
     * 获取最大重试次数
     * @return
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * 设置最大重试次数
     * @param maxRetryCount
     */
    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    /**
     * 获取失败重试间隔长度
     * @return
     */
    public int getRetryIntervalMillis() {
        return retryIntervalMillis;
    }

    /**
     * 设置失败重试间隔长度
     * @param retryIntervalMillis
     */
    public void setRetryIntervalMillis(int retryIntervalMillis) {
        this.retryIntervalMillis = retryIntervalMillis;
    }

    /**
     * 货物字符集编码
     * @return
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 设置字符集编码
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 获取User-Agent
     * @return
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 设置User-Agent
     * @param userAgent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * 获取缓冲大小票
     * @return
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * 设置缓冲大小
     * @param bufferSize
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * 设置Cookie
     * @param cookieJar
     */
    public void setCookieJar(CookieJar cookieJar){
        ReflectionUtility.setValue(this.client,"cookieJar", cookieJar);
    }
}
