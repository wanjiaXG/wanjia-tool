package com.wanjiaxg.http;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.IOUtility;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebResponse {

    private Response response;

    private WebClient client;

    public WebResponse(Response response, WebClient client) {
        this.response = response;
        this.client = client;
    }

    String body(IWebResultCallback callback){
        String result = null;
        if(this.response != null){
            ResponseBody body = null;
            InputStreamReader isr = null;
            try{
                StringBuilder sb = new StringBuilder();
                body = this.response.body();
                isr = new InputStreamReader(body.byteStream(), this.client.getEncoding());
                callback.onReady(this.response.code(), this.response.headers().toMultimap());
                char[] buffer = new char[this.client.getBufferSize()];
                int length = 0;
                while ((length = isr.read(buffer)) > 0){
                    sb.append(buffer, 0, length);
                }
                result = sb.toString();
                callback.onSuccess(result);
            }catch (Exception e){
                callback.onError(e.getMessage());
            }finally {
                IOUtility.closeStream(isr);
                IOUtility.closeStream(body);
            }
        }else {
            callback.onError("Connection failed");
        }
        return result;
    }

    public String body(){
        return body(client.getWebResultCallback());
    }

    boolean save(String file, IWebSaveFileCallback callback){
        boolean success = false;
        if(this.response != null){
            InputStream is = null;
            FileOutputStream fos = null;
            String tmpFile = System.currentTimeMillis() + this.client.getTmpFileSuffix();
            ResponseBody body = null;
            try{
                if(!FileUtility.initFileDirectory(file)){
                    throw new Exception("Can' write the file " + file);
                }
                body = this.response.body();
                if(body == null){
                    throw new Exception("Connection failed");
                }
                is = body.byteStream();
                fos = new FileOutputStream(tmpFile);
                long contentLength = body.contentLength();
                if(contentLength < 0){
                    throw new Exception("Empty response");
                }
                callback.onReady(this.response.code(), this.response.headers().toMultimap(), contentLength);
                byte[] buffer = new byte[this.client.getBufferSize()];
                int length = 0;
                while ((length = is.read(buffer)) > 0){
                    fos.write(buffer, 0, length);
                    fos.flush();
                    callback.onReading(length);
                }
                fos.close();
                success = FileUtility.moveFile(tmpFile, file);
                callback.onSuccess();;
            }catch (Exception e){
                callback.onError(e.getMessage());
            }finally {
                IOUtility.closeStream(fos);
                IOUtility.closeStream(body);
                IOUtility.closeStream(is);
                FileUtility.deleteFile(tmpFile);
            }
        }else {
            callback.onError("Connection failed");
        }
        return success;
    }

    public boolean save(String file){
        return save(file, this.client.getWebSaveFileCallback());
    }

    public void close(){
        IOUtility.closeStream(this.response);
    }

}
