package com.wanjiaxg.http;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.IOUtility;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.*;

public class WebResponse {

    private Response response;

    private WebClient client;

    public WebResponse(Response response, WebClient client) {
        this.response = response;
        this.client = client;
    }

    @SuppressWarnings("ConstantConditions")
    public String getBody(){
        String result = null;
        if(checkResponseCode()){
            try {
                result = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                IOUtility.closeStream(response);
            }
        }
        return result;
    }

    @SuppressWarnings("ConstantConditions")
    void getBody(IWebResultCallback callback){
        if(checkResponseCode()){
            callback.onReady(response.code(), response.headers().toMultimap());
            try {
                callback.onSuccess(response.body().string());
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }finally {
                IOUtility.closeStream(response);
            }
        }else {
            callback.onError("获取失败");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public boolean saveFile(String file){
        boolean result = false;
        FileOutputStream fos = null;
        if(checkResponseCode()){
            try{
                InputStream is = response.body().byteStream();
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[this.client.getBufferSize()];
                int length = 0;
                while ((length = is.read(buffer)) > 0){
                    fos.write(buffer, 0, length);
                    fos.flush();
                }
                result = true;
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                IOUtility.closeStream(response);
                IOUtility.closeStream(fos);
            }
        }
        return result;
    }

    @SuppressWarnings("ConstantConditions")
    void saveFile(IWebSaveFileCallback callback){
        if(checkResponseCode()){
            try{
                callback.onReady(response.code(),
                        response.headers().toMultimap(),
                        response.body().contentLength());
                InputStream is = response.body().byteStream();
                byte[] buffer = new byte[this.client.getBufferSize()];
                int length = 0;
                while ((length = is.read(buffer)) > 0){
                    callback.onReading(length, buffer.clone());
                }
                callback.onSuccess();
            }catch (Exception e){
                callback.onError(e.getMessage());
            }finally {
                IOUtility.closeStream(response);
            }
        }else{
            callback.onError("获取失败");
        }
    }

    private boolean checkResponseCode(){
        return this.response != null &&
               this.response.code() >= 200 &&
               this.response.code() < 300;
    }

    public void close(){
        IOUtility.closeStream(this.response);
    }

    public Response getResponse(){
        return this.response;
    }

}
