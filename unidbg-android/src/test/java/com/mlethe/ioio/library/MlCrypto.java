/*
package com.mlethe.ioio.library;

import android.content.Context;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MlCrypto {
    public static final int a = 0;
    public static final int f3010a = 0;
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};


    static {
        System.loadLibrary("mlcrypto");
    }

    public static String a(String str, String str2) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        String str3 = "";
        try {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            String strEncodeToString = Base64.encodeToString(cipher.doFinal(bytes), 0);
            try {
                return strEncodeToString.replaceAll("\\s", "");
            } catch (Exception e) {
                str3 = strEncodeToString;
                e.printStackTrace();
                return str3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str3;
    }

    public static String b(String str) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            StringBuilder sb = new StringBuilder(bArrDigest.length * 2);
            for (byte b : bArrDigest) {
                sb.append("0123456789abcdef".charAt((b >> 4) & 15));
                sb.append("0123456789abcdef".charAt(b & 15));
            }
            return sb.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public static native double coordinateDistance(double d, double d4, double d5, double d6);

    public static byte[] decrypt(byte[] bytes) {
        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec("36E8dfZ6YyEf0C6f9d<1A{lSLP8oR96B".getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryptStr(byte[] bytes) {
        String dec = "";
        try {
            byte[] textBytes = Base64.decode(bytes, 0);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec("36E8dfZ6YyEf0C6f9d<1A{lSLP8oR96B".getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            dec = new String(cipher.doFinal(textBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dec.getBytes();
    }

    public static byte[] encrypt(byte[] textBytes) {
        String enc = "";
        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec("36E8dfZ6YyEf0C6f9d<1A{lSLP8oR96B".getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            enc = Base64.encodeToString(cipher.doFinal(textBytes), 0);
            enc = enc.replaceAll("\\s", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enc.getBytes(StandardCharsets.UTF_8);
    }

    public static native double[] gcj02ToWgs84(double d, double d4);

    public static native double[] gcj02ToWgs84Exact(double d, double d4);

    public static native String getAutoBillKey();

    public static native void init(Context context, String str);

    public static native double[] wgs84ToGcj02(double d, double d4);

}
*/
