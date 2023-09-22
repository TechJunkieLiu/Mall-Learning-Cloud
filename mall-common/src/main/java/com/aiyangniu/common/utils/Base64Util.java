package com.aiyangniu.common.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64工具类
 *
 * @author lzq
 * @date 2023/09/22
 */
public class Base64Util {

    /**
     * Base64加解密
     *
     * Java8提供的Base64拥有更好的效能
     * Windows默认为 GBK 编码，Linux默认为 UTF-8 编码。指定编码和解码格式，保证操作过程中不会出现中文乱码问题，如果不指定会使用环境默认编码格式
     */
    public static String base64Code(String data, Boolean isEncode){
        if (isEncode){
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(data.getBytes(StandardCharsets.UTF_8));
        }else {
            Base64.Decoder decoder = Base64.getDecoder();
            return new String(decoder.decode(data), StandardCharsets.UTF_8);
        }
    }

    /**
     * 图片转 Base64 字符串
     *
     * 如果需要将Base64字符串展示在html的img标签中，需要给字符串添加前缀 data:image/jpg;base64,
     */
    public static String image2Base64(String imgFilePath) {
        byte[] data = null;
        try {
            InputStream inputStream = new FileInputStream(imgFilePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * Base64 字符串转图片
     *
     * 对于非标准的 Base64 字符串（img 标签中的字符串），需要去掉特定的前缀后再转成图片，否则图片无法打开
     */
    public static void base642Image(String base64Str, String imgFilePath) {
        if (base64Str == null) {
            return;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(imgFilePath);
            byte[] bytes = decoder.decodeBuffer(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
