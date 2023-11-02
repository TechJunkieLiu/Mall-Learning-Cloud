package com.aiyangniu.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加解密工具类
 *
 * @author lzq
 * @date 2023/08/11
 */
public class SecretUtil {

    /**
     * MD5加密
     */
    public static String mdEncode(String origin){
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = md.digest(origin.getBytes());
            return new BASE64Encoder().encode(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * digest
     * 摘要加密
     */
    public static String digest(String origin){
        return SecureUtil.md5(origin);
    }

    /**
     * BASE64加解密
     */
    public static String base64Encode(String origin, Boolean isEncode){
        BASE64Encoder base64E = new BASE64Encoder();
        String encode = base64E.encode(origin.getBytes());
        if (isEncode){
            return encode;
        }else {
            BASE64Decoder base64D = new BASE64Decoder();
            byte[] bytes = null;
            try {
                bytes = base64D.decodeBuffer(encode);
            }catch (IOException e){
                e.printStackTrace();
            }
            assert bytes != null;
            return new String(bytes);
        }
    }

    /**
     * symmetric
     * 对称加解密，根据自定义密钥进行加密，如果秘钥长度不够16位会报错，且长度只能是16位、24位、32位
     */
    public static String symmetric(String origin, String key, Boolean isEncrypt) {
        // 生成密钥
        byte[] byteKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()).getEncoded();
        SymmetricCrypto aes = SecureUtil.aes(byteKey);
        String encryptBase64 = aes.encryptBase64(origin);
        if (isEncrypt) {
            return encryptBase64;
        } else {
            return aes.decryptStr(encryptBase64);
        }
    }

    /**
     * asymmetric
     * 不对称加解密
     */
    public static String asymmetric(String origin, Boolean isEncrypt){
        // 当使用无参构造方法时，HuTool将自动生成随机的公钥私钥密钥对
        RSA rsa = SecureUtil.rsa();
        // 获得私钥
        rsa.getPrivateKey();
        rsa.getPrivateKeyBase64();
        // 获得公钥
        rsa.getPublicKey();
        rsa.getPublicKeyBase64();
        // 加密
        String encryptData = rsa.encryptBase64(origin, KeyType.PrivateKey);
        if (isEncrypt){
            return encryptData;
        }else {
            return rsa.decryptStr(encryptData, KeyType.PublicKey);
        }
    }

    /**
     * SM3(国密3)加密
     * 签名算法，和MD5一样（对于应用层来说）
     */
    public static String sm3(String origin){
        return SmUtil.sm3(origin);
    }

    /**
     * SM4(国密4)加解密
     */
    public static String sm4(String origin, Boolean isEncrypt){
        SymmetricCrypto sm4 = SmUtil.sm4();
        String encryptHex = sm4.encryptHex(origin);
        if (isEncrypt){
            return encryptHex;
        }else {
            return sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        }
    }

    /**
     * SM4(国密4)加解密
     * 对称加密算法，和AES一样（对于应用层来说），密钥是128位，也就是16个字节
     */
    public static String sm4(String origin, String key, Boolean isEncrypt){
        SymmetricCrypto sc = SmUtil.sm4(key.getBytes());
        String encryptString = sc.encryptHex(origin);
        if (isEncrypt){
            return encryptString;
        }else {
            return sc.decryptStr(encryptString, CharsetUtil.CHARSET_UTF_8);
        }
    }
}
