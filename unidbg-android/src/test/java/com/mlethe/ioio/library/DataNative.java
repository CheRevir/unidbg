//
// Decompiled by Jadx - 645ms
//
package com.mlethe.ioio.library;

public abstract class DataNative {
    static {
        System.loadLibrary("mlcrypto");
    }

    public static native double coordinateDistance(double d, double d2, double d3, double d4);

    public static native byte[] decrypt(byte[] bArr);

    public static native byte[] decryptStr(byte[] bArr);

    public static native byte[] encrypt(byte[] bArr);

    public static native double[] gcj02ToWgs84(double d, double d2);

    public static native double[] gcj02ToWgs84Exact(double d, double d2);

    public static native String getAutoBillKey();

    //public static native boolean init(Context context, String str, String str2);

    public static native double[] wgs84ToGcj02(double d, double d2);
}
