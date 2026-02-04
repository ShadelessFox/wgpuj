package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUDepthStencilState;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record DepthStencilState() implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUDepthStencilState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        throw new UnsupportedOperationException();
    }
}
