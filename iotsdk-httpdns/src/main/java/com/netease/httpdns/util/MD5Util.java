package com.netease.httpdns.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final String MD5 = "MD5";

    public static String getMD5(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for(byte b : result) {
                int num = b & 255;
                String hex = Integer.toHexString(num);
                if (hex.length() == 1) {
                    sb.append(0);
                }

                sb.append(hex);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException var10) {
            return null;
        } catch (Exception var11) {
            return null;
        }
    }
}