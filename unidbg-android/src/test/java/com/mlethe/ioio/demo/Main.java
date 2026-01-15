package com.mlethe.ioio.demo;

import com.github.unidbg.AndroidEmulator;
import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.ModuleListener;
import com.github.unidbg.arm.backend.Backend;
import com.github.unidbg.debugger.Debugger;
import com.github.unidbg.debugger.FunctionCallListener;
import com.github.unidbg.linux.android.AndroidEmulatorBuilder;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.linux.android.dvm.*;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.pointer.UnidbgPointer;
import unicorn.Arm64Const;
import unicorn.ArmConst;

import java.io.File;

public class Main extends AbstractJni {

    public static void main(String[] args) {
        Main main = new Main();
    }

    private final AndroidEmulator emulator;
    private final VM vm;
    private final DvmClass nativeClass;

    public Main(){
        emulator = AndroidEmulatorBuilder.for32Bit()
                .setProcessName("com.anjuke.android.app")
                .build();
        emulator.getSyscallHandler().addIOResolver(new FileResolver());

        Debugger debugger = emulator.attach();
        System.out.println("函数调用关系追踪器已附加，结果将输出到日志文件。");
        debugger.traceFunctionCall(null, new FunctionCallListener() {
            private int depth = 0;
            private String getPrefix(int currentDepth) {
                if (currentDepth <= 0) {
                    return "";
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < currentDepth - 1; i++) {
                    sb.append("│  ");
                }
                sb.append("├─ ");
                return sb.toString();
            }
            @Override
            public void onCall(Emulator<?> emulator, long callerAddress, long functionAddress) {
                String prefix = getPrefix(depth + 1);
                String details = emulator.getUnwinder().formatAddressDetails(functionAddress);
                //traceStream.printf("%sCALL -> %s%n", prefix, details);
                System.out.println(String.format("%sCALL -> %s%n",prefix,details));
                depth++;
            }
            @Override
            public void postCall(Emulator<?> emulator, long callerAddress, long functionAddress, Number[] args) {
                depth--;
                String prefix = getPrefix(depth + 1);
                String details = emulator.getUnwinder().formatAddressDetails(functionAddress);
                Backend backend = emulator.getBackend();
                Number retVal = emulator.is64Bit() ? backend.reg_read(Arm64Const.UC_ARM64_REG_X0) : backend.reg_read(ArmConst.UC_ARM_REG_R0);
                long retValLong = retVal.longValue();
                // 尝试将返回值作为指针解析
                String retValFormatted = String.format("0x%x", retValLong);
                UnidbgPointer pointer = UnidbgPointer.pointer(emulator, retValLong);
                if (pointer != null) {
                    String cstring = safeReadCString(pointer);
                    // 如果是一个可打印的字符串，则附加到日志中
                    if (isPrintable(cstring)) {
                        retValFormatted += String.format(" -> \"%s\"", cstring);
                    }
                }
                //traceStream.printf("%sRET  <- %s, ret=%s%n", prefix, details, retValFormatted);
                System.out.println(String.format("%sRET  <- %s, ret=%s%n", prefix, details, retValFormatted));
            }
        });

        Memory memory = emulator.getMemory();
        memory.addModuleListener(new ModuleListener() {
            @Override
            public void onLoaded(Emulator<?> emulator, Module module) {
                if(module.name.equals("libmlcrypto.so")){
                    //emulator.traceCode(module.base,module.base+module.size);
                }
            }
        });
        memory.setLibraryResolver(new AndroidResolver(23));
        vm = emulator.createDalvikVM(new File("./unidbg-android/src/test/resources/app-release/app-release.apk"));
        vm.setVerbose(true);
        vm.setJni(this);

        System.out.println(vm.getPackageName());

        DalvikModule dm = vm.loadLibrary("mlcrypto",false);
        System.out.println(dm.getModule().getPath());
        System.out.println(Long.toHexString(dm.getModule().base));
        System.out.println(Long.toHexString(dm.getModule().size));
        System.out.println(Long.toHexString(dm.getModule().size));
        nativeClass = vm.resolveClass("com/mlethe/ioio/library/DataNative");
        System.out.println(nativeClass.getName());
        dm.callJNI_OnLoad(emulator);
    }

    @Override
    public DvmObject<?> callObjectMethodV(BaseVM vm, DvmObject<?> dvmObject, String signature, VaList vaList) {
        if("android/app/Application->getPackageResourcePath()Ljava/lang/String;".equals(signature)){
            return new StringObject(vm,"/data/app/com.mlethe.ioio.demo/base.apk");
        }
        return super.callObjectMethodV(vm, dvmObject, signature, vaList);
    }

    private static String safeReadCString(UnidbgPointer p) {
        try {
            return p.getString(0);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isPrintable(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        int printableChars = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 32 && c <= 126) || Character.isWhitespace(c)) {
                printableChars++;
            }
        }
        return s.length() >= 2 && (double) printableChars / s.length() > 0.8;
    }
}
