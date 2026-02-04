package sh.adelessfox.wgpuj.util;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public interface WgpuStruct {
    MemoryLayout nativeLayout();

    void toNative(SegmentAllocator allocator, MemorySegment segment);

    default MemorySegment toNative(SegmentAllocator allocator) {
        var segment = allocator.allocate(nativeLayout());
        toNative(allocator, segment);
        return segment;
    }
}
