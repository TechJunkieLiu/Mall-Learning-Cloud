package com.aiyangniu.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 加解密工具类
 *
 * @author lzq
 * @date 2023/09/21
 */
public class EncryptUtil {

    public static String BASE64Encrypt;
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);
    private final static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String md5ToString(String signed) {
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] res = signed.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
            mdTemp.update(res);
            byte[] md = mdTemp.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static byte[] md5(String str) {
        try {
            byte[] res = str.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
            mdTemp.update(res);
            byte[] hash = mdTemp.digest();
            return hash;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5值计算
     * MD5的算法在 RFC1321 中定义：
     * 检验实现是否正确：
     * MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
     * MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
     * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
     * MD5 ("message digest") = f96b697d7cb7938d525a2f31aaf161d0
     *
     * @param str 源字符串
     * @return MD5值
     */
    public static byte[] md5EncryptReturnHexDigitsByteArray(String str) {
        try {
            byte[] res = str.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5".toUpperCase());
            mdTemp.update(res);
            return mdTemp.digest();
        } catch (Exception e) {
            return null;
        }
    }

    public static String md5EncryptReturnString(String str) {

        byte[] b = md5EncryptReturnHexDigitsByteArray(str);

        StringBuilder sb = new StringBuilder();
        for (int value : b) {
            int n = value;
            if (n < 0) {
                n = 256 + n;
            }
            int d1 = n / 16;
            int d2 = n % 16;
            sb.append(HEX_DIGITS[d1]);
            sb.append(HEX_DIGITS[d2]);
        }
        return sb.toString();

    }

    /**
     * 加密后解密
     */
    public static String jm(byte[] inStr) {
        String newStr = new String(inStr);
        char[] a = newStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }

    /**
     * BASE64加密MD5
     */
    public static String base64Encrypt(byte[] key) throws Exception {
        String edata = null;
        try {
            edata = (new BASE64Encoder()).encodeBuffer(key).trim();
        } catch (Exception e) {
            throw new Exception(e.getMessage() + "BASE64编码错误！key=" + new String(key) + ", error=" + e.getMessage());
        }
        return edata;
    }

    /**
     * BASE64解密
     */
    public static byte[] base64Decrypt(String data) {
        if (data == null) {
            return null;
        }
        byte[] edata = null;
        try {
            edata = (new BASE64Decoder()).decodeBuffer(data);
            return edata;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 签名加密
     */
    public static String encryptSigned(String signed) throws Exception {

        try {
            byte[] md5SignStr = md5EncryptReturnHexDigitsByteArray(signed);
            return base64Encrypt(md5SignStr);
        }catch(Exception e) {
            throw new Exception(e.getMessage()+ "BASE64或MD5加密签名错误！signed=" + signed + ", error=" + e.getMessage());
        }
    }
}
