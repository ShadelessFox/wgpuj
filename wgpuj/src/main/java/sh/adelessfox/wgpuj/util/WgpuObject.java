package sh.adelessfox.wgpuj.util;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;

public interface WgpuObject extends WgpuStruct, AutoCloseable {
    @Override
    default MemoryLayout nativeLayout() {
        return ValueLayout.ADDRESS;
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        segment.set(ValueLayout.ADDRESS, 0, segment());
    }

    MemorySegment segment();

    @Override
    void close();
}
