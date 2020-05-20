package com.wanjiaxg.http;

import com.wanjiaxg.utility.FileUtility;
import com.wanjiaxg.utility.IOUtility;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@SuppressWarnings("ALL")
public class WebResponse {

    /**
     * 响应对象
     */
    private Response response;

    /**
     * 客户端
     */
    private WebClient client;

    /**
     * 响应码
     */
    private int code = 0;

    /**
     * 构造函数
     * @param response  响应对象
     * @param client    客户端
     */
    WebResponse(Response response, WebClient client) {
        this.response = response;
        this.client = client;
        initCode();
    }

    /**
     * 初始化响应码
     */
    private void initCode() {
        if(this.response != null){
            try{
                code = response.code();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取响应体
     * @return
     */
    public String getBody(){
        String result = null;
        try {
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtility.closeStream(response);
        }
        return result;
    }

    /**
     * 保存文件到本地
     * @param file 文件路径
     * @return
     */
    public boolean saveFile(String file){
        boolean result = false;
        FileOutputStream fos = null;
        if(code == 200){
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
                FileUtility.deleteFile(file);
            }finally {
                IOUtility.closeStream(response);
                IOUtility.closeStream(fos);
            }
        }
        return result;
    }

    /**
     * 关闭响应对象
     */
    public void close(){
        IOUtility.closeStream(this.response);
    }

    /**
     * 获得响应
     * @return
     */
    public Response getResponse(){
        return this.response;
    }
}
