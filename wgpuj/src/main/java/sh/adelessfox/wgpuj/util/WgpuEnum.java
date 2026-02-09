package sh.adelessfox.wgpuj.util;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;

@SuppressWarnings("unused")
public interface WgpuEnum<T extends Enum<T> & WgpuEnum<T>> extends WgpuStruct {
    static <T extends Enum<T> & WgpuEnum<T>> T valueOf(int value, Class<T> cls) {
        for (T constant : cls.getEnumConstants()) {
            if (constant.value() == value) {
                return constant;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value + " in " + cls.getName());
    }

    @Override
    default MemoryLayout nativeLayout() {
        return ValueLayout.JAVA_INT;
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        segment.set(ValueLayout.JAVA_INT, 0, value());
    }

    int value();
}
