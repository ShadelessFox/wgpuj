package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBlendComponent;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface BlendComponent extends WgpuStruct {
    default BlendOperation operation() {
        return BlendOperation.ADD;
    }

    default BlendFactor srcFactor() {
        return BlendFactor.ONE;
    }

    default BlendFactor dstFactor() {
        return BlendFactor.ZERO;
    }

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUBlendComponent.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUBlendComponent.operation(segment, operation().value());
        WGPUBlendComponent.srcFactor(segment, srcFactor().value());
        WGPUBlendComponent.dstFactor(segment, dstFactor().value());
    }
}
