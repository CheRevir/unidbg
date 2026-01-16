/*
package com.mlethe.ioio;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class App extends Application implements InvocationHandler {
    private static final int GET_SIGNATURES = 0x00000040;

    private Object base;
    //private byte[][] sign;
    private String appPkgName = "";

    @Override
    protected void attachBaseContext(Context base) {
        hook(base);
        super.attachBaseContext(base);
    }

    @Override
    public Object invoke(Object proxy, @NonNull Method method, Object[] args) throws Throwable {
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            Integer flag = (Integer) args[1];
            if ((flag & GET_SIGNATURES) != 0 && appPkgName.equals(pkgName)) {
                PackageInfo info = (PackageInfo) method.invoke(base, args);
                info.signatures = new Signature[1];
                for (int i = 0; i < info.signatures.length; i++) {
                    info.signatures[i] = new Signature("");
                }
                return info;
            }
        }
        return method.invoke(base, args);
    }

    private void hook(Context context) {
        try {
            */
/*String data = "### Signatures Data ###";
            DataInputStream is = new DataInputStream(new ByteArrayInputStream(Base64.decode(data, Base64.DEFAULT)));
            byte[][] sign = new byte[is.read() & 0xFF][];
            for (int i = 0; i < sign.length; i++) {
                sign[i] = new byte[is.readInt()];
                is.readFully(sign[i]);
            }*//*


            // 获取全局的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod =
                    activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面原始的sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            this.base = sPackageManager;
            //this.sign = sign;
            this.appPkgName = context.getPackageName();

            Object proxy = Proxy.newProxyInstance(
                    iPackageManagerInterface.getClassLoader(),
                    new Class<?>[]{iPackageManagerInterface},
                    this);

            // 1. 替换掉ActivityThread里面的 sPackageManager 字段
            sPackageManagerField.set(currentActivityThread, proxy);

            // 2. 替换 ApplicationPackageManager里面的 mPM对象
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
            System.out.println("PmsHook success.");
        } catch (Exception e) {
            System.err.println("PmsHook failed.");
            e.printStackTrace();
        }
    }
}
*/
