package sh.adelessfox.wgpuj.util;

import org.immutables.value.Value;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public interface WgpuStruct {
    @Value.Derived
    MemoryLayout nativeLayout();

    @Value.Derived
    void toNative(SegmentAllocator allocator, MemorySegment segment);

    @Value.Derived
    default MemorySegment toNative(SegmentAllocator allocator) {
        var segment = allocator.allocate(nativeLayout());
        toNative(allocator, segment);
        return segment;
    }
}
