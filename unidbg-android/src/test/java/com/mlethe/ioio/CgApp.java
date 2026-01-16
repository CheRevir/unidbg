/*
package com.mlethe.ioio;

import android.ace.ActivityThread;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import com.mlethe.ioio.library.MlCrypto;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class CgApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String path = getPackageResourcePath();
        try {
            String p = ActivityThread.currentApplication().getPackageResourcePath();
            Log.e("CeRe", "onCreate: ");
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String a = new String(MlCrypto.decryptStr("38NLxsJn98dwAurQB9fVjg==".getBytes()));

        killPM(getPackageName(),"aa8202923082017a020101300d06092a864886f70d01010b0500300e310c300a0603550406130343484e3020170d3235303631333032323630375a180f32303530303630373032323630375a300e310c300a0603550406130343484e30820122300d06092a864886f70d01010105000382010f003082010a0282010100da2086d2a524ab8a68cfb94557163979c38f12c4054c90e60cc740dca335dc03a543db22eccee9ae155639dc0496e47a56a29e129e9cf27d6870a28a89cbd744f9119f15fbd21ebf548c67f5bfe87d8e178799e37a786429fa8acd76e93cbc2ef10886066abc3fab8f3927939e39957dbb432220339f3706ce20fdf20c046f9f64aaa4e9da9560d6b68ae3b25baa12589b8631596b7dd9f033b7a70bc090c7928548077a109cd437bbcd0890047f10db1bd7225c3a99899b111c3bd6a27bde72a0da0af108786341dc713caf687b15a145550d2bf633474ed4145a48cbc845d1000083f2fde0bd9a066d4da57781dd7d2926e80e51de2df38262c329821bac990203010001300d06092a864886f70d01010b0500038201010041cb4bbe39869cdc9f8ba238831182dff5038d93743a414ad16390e70c3cff5b28fe1b58e3b6c8b969299b99065239165804a5cdad190621b4b8bbc0c7ce0235024b14785565da96bd9efbf8ccddffd18d711ded81927ee63394e7599547db9270031f31550840b561a472a457af85042059eda2512281672c65c470319ddd901dc85d8f5709a18dcf6339911e81697063f91f70344822fe039e3183cf0aeb8d7f5ef42a2c0ce884d9584f7a8674df3fd1b320a49ccc56ca2951f10525cc6bd3a80a03fd3824072ded33a63666078caf43fb860232b1a81f43cb13e62ea6894512ec66279a69d5ec5aa68893dad6104780b360cc8961cf11e72d932623592bce");
    }

    private static void killPM(String packageName, String signatureData) {
        Signature fakeSignature = new Signature(signatureData);
        Parcelable.Creator<PackageInfo> originalCreator = PackageInfo.CREATOR;
        Parcelable.Creator<PackageInfo> creator = new Parcelable.Creator<PackageInfo>() {
            @Override
            public PackageInfo createFromParcel(Parcel source) {
                PackageInfo packageInfo = originalCreator.createFromParcel(source);
                if (packageInfo.packageName.equals(packageName)) {
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        packageInfo.signatures[0] = fakeSignature;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (packageInfo.signingInfo != null) {
                            android.content.pm.Signature[] signaturesArray = packageInfo.signingInfo.getApkContentsSigners();
                            if (signaturesArray != null && signaturesArray.length > 0) {
                                signaturesArray[0] = fakeSignature;
                            }
                        }
                    }
                }
                return packageInfo;
            }

            @Override
            public PackageInfo[] newArray(int size) {
                return originalCreator.newArray(size);
            }
        };
        try {
            findField(PackageInfo.class, "CREATOR").set(null, creator);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("Landroid/os/Parcel;", "Landroid/content/pm", "Landroid/app");
        }
        try {
            Object cache = findField(PackageManager.class, "sPackageInfoCache").get(null);
            //noinspection ConstantConditions
            cache.getClass().getMethod("clear").invoke(cache);
        } catch (Throwable ignored) {
        }
        try {
            Map<?, ?> mCreators = (Map<?, ?>) findField(Parcel.class, "mCreators").get(null);
            //noinspection ConstantConditions
            mCreators.clear();
        } catch (Throwable ignored) {
        }
        try {
            Map<?, ?> sPairedCreators = (Map<?, ?>) findField(Parcel.class, "sPairedCreators").get(null);
            //noinspection ConstantConditions
            sPairedCreators.clear();
        } catch (Throwable ignored) {
        }
    }

    private static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null || clazz.equals(Object.class)) {
                    break;
                }
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field;
                } catch (NoSuchFieldException ignored) {
                }
            }
            throw e;
        }
    }
}
*/
