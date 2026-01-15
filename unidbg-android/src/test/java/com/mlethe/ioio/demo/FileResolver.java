package com.mlethe.ioio.demo;

import com.github.unidbg.Emulator;
import com.github.unidbg.file.FileResult;
import com.github.unidbg.file.IOResolver;
import com.github.unidbg.file.linux.AndroidFileIO;
import com.github.unidbg.linux.file.ByteArrayFileIO;
import com.github.unidbg.linux.file.SimpleFileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileResolver implements IOResolver<AndroidFileIO> {
    @Override
    public FileResult<AndroidFileIO> resolve(Emulator<AndroidFileIO> emulator, String pathname, int oflags) {
        if ("/data/app/com.mlethe.ioio.demo/base.apk".equals(pathname)) {
            return FileResult.success(new SimpleFileIO(oflags, new File("./unidbg-android/src/test/resources/app-release/app-release.apk"), "/data/app/com.mlethe.ioio.demo/base.apk"));
        }
        if ("/proc/self/maps".equals(pathname)) {
            final String APK_PATH = "/data/app/com.mlethe.ioio.demo/base.apk";
            String maps = "12000000-12153000 r--s 00000000 08:0d 4554758 " + APK_PATH + "\n";
            File file = new File("./unidbg-android/src/test/resources/app-release/maps");
            byte[] bytes = new byte[(int) file.length()];
            try {
                FileInputStream stream = new FileInputStream(file);
                stream.read(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //System.out.println(new String(bytes, StandardCharsets.UTF_8));
            return FileResult.success(new ByteArrayFileIO(oflags, pathname, bytes));
        }
        return null;
    }
}
