package com.mobile.android.withings.service.connection;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignTool {

    public static String generateHmacSHA1Signature(String data, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(OAuth.ENC), OAuth.HMAC_SHA1);
            Mac mac = Mac.getInstance(OAuth.HMAC_SHA1);
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(data.getBytes(OAuth.ENC));
            return Base64.encodeToString(hmacData, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
