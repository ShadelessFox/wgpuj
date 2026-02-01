package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPURenderPassDepthStencilAttachment;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record RenderPassDepthStencilAttachment() implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPURenderPassDepthStencilAttachment.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        throw new UnsupportedOperationException();
    }
}
