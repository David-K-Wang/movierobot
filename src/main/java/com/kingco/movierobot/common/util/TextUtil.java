package com.kingco.movierobot.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

public class TextUtil {
    private static String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateDuplicateNameSuffix(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
        }
        return sb.toString();
    }

    public static int getAllCharLen() {
        return ALL_CHAR.length();
    }

    /**
     * 将original安全地从iso-8859-1转为utf-8
     * 
     * @param original
     * @return
     */
    public static String iso8859ToUtf8(String original) {
        try {
            String newString = transcode(original, "iso-8859-1", "utf-8");
            return newString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String transcode(String original, String fromEncoding, String toEncoding)
            throws UnsupportedEncodingException {
        if (original == null) {
            return null;
        }
        String newString = new String(original.getBytes(fromEncoding), toEncoding);
        return newString;
    }

    public static String urlEncodeUtf8(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String urlDecodeUtf8(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
