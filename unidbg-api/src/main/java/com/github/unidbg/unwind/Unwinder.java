package com.github.unidbg.unwind;

import com.github.unidbg.Emulator;
import com.github.unidbg.Module;
import com.github.unidbg.Symbol;
import com.github.unidbg.arm.AbstractARMDebugger;
import com.github.unidbg.memory.MemRegion;
import com.github.unidbg.memory.Memory;
import com.github.unidbg.memory.SvcMemory;
import com.github.unidbg.pointer.UnidbgPointer;
import com.github.zhkl0228.demumble.DemanglerFactory;
import com.github.zhkl0228.demumble.GccDemangler;

public abstract class Unwinder {

    public static final int SYMBOL_SIZE = 0x1000;

    protected final Emulator<?> emulator;

    protected Unwinder(Emulator<?> emulator) {
        this.emulator = emulator;
    }

    public abstract Frame createFrame(UnidbgPointer ip, UnidbgPointer fp);

    protected abstract Frame unw_step(Emulator<?> emulator, Frame frame);

    protected abstract String getBaseFormat();

    public final void unwind() {
        Memory memory = emulator.getMemory();
        String maxLengthSoName = memory.getMaxLengthLibraryName();
        boolean hasTrace = false;

        Frame frame = null;
        while ((frame = unw_step(emulator, frame)) != null) {
            if (frame.isFinish()) {
                if (!hasTrace) {
                    System.out.println("Decode backtrace finish");
                }
                return;
            }

            hasTrace = true;
            printFrameElement(maxLengthSoName, memory, frame.ip);
        }

        if (!hasTrace) {
            System.err.println("Decode backtrace failed.");
        }
    }

    private void printFrameElement(String maxLengthSoName, Memory memory, UnidbgPointer ip) {
        final int maxLength = maxLengthSoName.length();
        SvcMemory svcMemory = emulator.getSvcMemory();
        MemRegion region = svcMemory.findRegion(ip.peer);
        Module module = region != null ? null : AbstractARMDebugger.findModuleByAddress(emulator, ip.peer);
        StringBuilder sb = new StringBuilder();
        String format = getBaseFormat();
        if (module != null) {
            sb.append(String.format(format, module.base)).append(String.format(format, ip.peer));
            sb.append(String.format("[%" + maxLength + "s]", module.name));
            sb.append(String.format("[0x%0" + Long.toHexString(memory.getMaxSizeOfLibrary()).length() + "x]", ip.peer - module.base));

            Symbol symbol = module.findClosestSymbolByAddress(ip.peer, false);
            if (symbol != null && ip.peer - symbol.getAddress() <= SYMBOL_SIZE) {
                GccDemangler demangler = DemanglerFactory.createDemangler();
                sb.append(" ").append(demangler.demangle(symbol.getName())).append(" + 0x").append(Long.toHexString(ip.peer - (symbol.getAddress() & ~1)));
            }
        } else {
            sb.append(String.format(format, 0)).append(String.format(format, ip.peer));
            if (region == null) {
                sb.append(String.format("[%" + maxLength + "s]", "0x" + Long.toHexString(ip.peer)));
            } else {
                sb.append(String.format("[%" + maxLength + "s]", region.getName().substring(0, Math.min(maxLength, region.getName().length()))));
            }
            if (ip.peer >= svcMemory.getBase()) {
                sb.append(String.format("[0x%0" + Long.toHexString(memory.getMaxSizeOfLibrary()).length() + "x]", ip.peer - svcMemory.getBase()));
            }
        }
        System.out.println(sb);
    }

    /**
     * 将给定的内存地址格式化为包含详细信息、可读的字符串。
     * 这个方法会尝试解析地址，并提供尽可能多的上下文信息，如模块名、函数名（符号）、偏移量等。
     *
     * @param address 要格式化的绝对内存地址。
     * @return 一个包含地址详细信息的格式化字符串。
     */
    public String formatAddressDetails(long address) {
        // 1. 尝试根据地址查找其所属的模块（例如，一个 .so 文件）。
        Module module = emulator.getMemory().findModuleByAddress(address);

        // 2. 如果地址位于一个已加载的模块内：
        if (module != null) {
            // 2.1. 在模块中查找离该地址最近的符号（即函数或全局变量的名称）。
            //      `true` 参数表示也查找非导出的内部符号。
            Symbol symbol = module.findClosestSymbolByAddress(address, true);

            // 2.2. 如果找到了一个符号：
            if (symbol != null) {
                // 2.2.1. 创建一个 demangler 实例，用于将 C++ "mangled"（混淆）的符号名还原为可读的函数签名。
                GccDemangler demangler = DemanglerFactory.createDemangler();
                // 2.2.2. 对符号名进行 demangle 操作。
                String demangledName = demangler.demangle(symbol.getName());
                // 2.2.3. 计算当前地址相对于符号起始地址的偏移量。
                long offset = address - symbol.getAddress();
                // 2.2.4. 返回最详细的格式化字符串，例如："[libnative.so] JNI_OnLoad + 0x10 (at 0x...)"
                return String.format("[%s] %s + 0x%x (at 0x%x)", module.name, demangledName, offset, address);
            } else {
                // 2.3. 如果在模块内但没有找到具体的符号，则将其视为一个未命名的子程序（subroutine）。
                //      计算地址相对于模块基地址的偏移量。
                long offset = address - module.base;
                // 2.3.1. 返回一个通用的子程序格式，例如："[libnative.so] sub_c80 (at 0x...)"
                return String.format("[%s] sub_%x (at 0x%x)", module.name, offset, address);
            }
        }

        // 3. 如果地址不属于任何模块，则尝试查找它是否位于一个已知的内存区域中（例如，栈或堆）。
        MemRegion region = emulator.getSvcMemory().findRegion(address);
        if (region != null) {
            // 3.1. 如果找到了，返回区域名称，例如："[stack] (at 0x...)"
            return String.format("[%s] (at 0x%x)", region.getName(), address);
        }

        // 4. 如果以上所有尝试都失败了，返回一个表示“未知”的通用格式。
        return String.format("[unknown] (at 0x%x)", address);
    }

}
