package com.ericwyn.juzcar.server.tml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * Juzcar Doc Server 返回的页面应该全部都是服务端渲染完成的，主要考虑如下
 *
 *      Doc Server 还提供文档导出服务
 *      如果是以接口 ajax 请求 api 信息并展示的话，不利于后期导出
 *
 * 渲染之后的 HTML 可以直接由 Doc Server 依赖的 Ezerver 直接返回
 * 而且 HTML 可以缓存在内存，不需要每次请求的时候都渲染一遍
 *
 * Created by Ericwyn on 19-3-4.
 */
public class HttpTemple {
    // String 其实不太环保？？？但是 Replace 方法的话 StringBuild 和 StringBuffer 似乎没办法解决
    private String templeStr ="";

    // 这里分为两个 templeStr，具体的替换操作放在了 resHtml
    // templeStr 作为原始记录保存，这样的话这个 HttpTemple 就可以多次使用了
    // 只需要调用 clearReplace 接口就可以恢复 resHtml
    private String resHtml = "";

    // 通过字符串创建 Temple
    public HttpTemple(String sbf){
        this.templeStr = sbf;
        this.resHtml = sbf;
    }

    // 通过文件创建 Temple
    public HttpTemple(File file){
        FileInputStream inputStream = null;
        try {
            templeStr = "";
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[2048];
            int length = -1;
            while ((length = inputStream.read(bytes)) != -1){
                templeStr += new String(bytes,0, length);
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        resHtml = templeStr;
    }

    /**
     * 渲染某些元素
     * 因为替换的模板内容是 {{.KEY}}，所以 key 不包含 . 和 {{}}
     *
     * @param key
     * @param value
     * @return
     */
    public HttpTemple replace(String key, String value){
        if (key != null && value != null){
            this.resHtml = resHtml.replaceAll("\\{\\{\\."+key+"}}", value);
        }
        return this;
    }

    /**
     * 清除已渲染的内容，使模板恢复原来状态
     * @return
     */
    public HttpTemple clearReplace(){
        this.resHtml = templeStr;
        return this;
    }

    /**
     * 获取渲染之后的 HTML
     * @return
     */
    public String string(){
        return this.resHtml;
    }
}
