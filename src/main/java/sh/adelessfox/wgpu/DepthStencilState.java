package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUDepthStencilState;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record DepthStencilState() implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUDepthStencilState.layout();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        throw new UnsupportedOperationException();
    }
}
