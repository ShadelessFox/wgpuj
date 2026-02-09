package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBlendState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface BlendState extends WgpuStruct {
    BlendComponent color();

    BlendComponent alpha();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUBlendState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        color().toNative(allocator, WGPUBlendState.color(segment));
        alpha().toNative(allocator, WGPUBlendState.alpha(segment));
    }
}
