package sh.adelessfox.wgpu.util;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public interface WgpuStruct {
    MemoryLayout nativeLayout();

    MemorySegment toNative(SegmentAllocator allocator);
}
