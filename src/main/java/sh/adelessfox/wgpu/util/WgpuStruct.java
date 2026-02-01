package sh.adelessfox.wgpu.util;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public interface WgpuStruct {
    MemorySegment toNative(SegmentAllocator allocator);
}
