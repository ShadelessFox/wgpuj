package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUBlendState;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record BlendState(
    BlendComponent color,
    BlendComponent alpha
) implements WgpuStruct {
    public static BlendStateBuilder builder() {
        return new BlendStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUBlendState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        color.toNative(allocator, WGPUBlendState.color(segment));
        alpha.toNative(allocator, WGPUBlendState.alpha(segment));
    }
}
