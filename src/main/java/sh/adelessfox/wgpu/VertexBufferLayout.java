package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record VertexBufferLayout() implements WgpuStruct {
    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        throw new UnsupportedOperationException();
    }
}
