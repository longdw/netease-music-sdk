package com.netease.httpdns.util;

import android.text.TextUtils;
import android.util.Base64;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

public class RSASignature {
    private static final String RSA = "RSA";
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String UTF_8 = "UTF-8";

    private RSASignature() {
    }

    public static boolean verify(String key, String content, String sign) throws Exception {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(sign)) {
            PublicKey publicKey = getPublicKey(key);
            if (publicKey == null) {
                return false;
            } else {
                Signature signature = Signature.getInstance("MD5withRSA");
                signature.initVerify(publicKey);
                signature.update(content.getBytes());
                return signature.verify(base64Decode(sign.getBytes()));
            }
        } else {
            return false;
        }
    }

    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = base64Decode(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] base64Decode(String encoded) throws Exception {
        return Base64.decode(encoded, 0);
    }

    public static byte[] base64Decode(byte[] encoded) throws Exception {
        return Base64.decode(encoded, 0);
    }
}