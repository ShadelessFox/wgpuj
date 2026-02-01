package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUVertexBufferLayout;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record VertexBufferLayout() implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUVertexBufferLayout.layout();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        throw new UnsupportedOperationException();
    }
}
