package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUBlendComponent;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record BlendComponent(
    BlendOperation operation,
    BlendFactor srcFactor,
    BlendFactor dstFactor
) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUBlendComponent.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUBlendComponent.operation(segment, operation.value());
        WGPUBlendComponent.srcFactor(segment, srcFactor.value());
        WGPUBlendComponent.dstFactor(segment, dstFactor.value());
    }
}
