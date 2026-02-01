package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu_native.WGPUBlendComponent;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record BlendComponent(
    BlendOperation operation,
    BlendFactor srcFactor,
    BlendFactor dstFactor
) {
    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUBlendComponent.allocate(allocator);
        WGPUBlendComponent.operation(segment, operation.value());
        WGPUBlendComponent.srcFactor(segment, srcFactor.value());
        WGPUBlendComponent.dstFactor(segment, dstFactor.value());
        return segment;
    }
}
