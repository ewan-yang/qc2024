package com.tecpie.platform.common.util;

import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

/**
 * 字符串工具类
 */
public class Str2HexUtil {

    private Str2HexUtil() {
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String binToHex(byte[] bytes) {
        return Hex.toHexString(bytes).toUpperCase();
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hex 字节数组
     * @return 十六进制字符串
     */
    public static byte[] hexToBin(String hex) {
        return Hex.decode(hex);
    }

    /**
     * 字节数组转UTF8字符串
     *
     * @param bytes 字节数组
     * @return UTF8字符串
     */
    public static String binToStr(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * UTF8字符串转字节数组
     *
     * @param str UTF8字符串
     * @return 字节数组
     */
    public static byte[] strToBin(String str) {
        return Strings.toUTF8ByteArray(str);
    }

    public static byte[] generate16BytesKey(String hexString) {
        int len = hexString.length();
        byte[] byteArray = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }
}
