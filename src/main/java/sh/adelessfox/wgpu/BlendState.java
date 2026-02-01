package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUBlendState;

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
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUBlendState.allocate(allocator);
        WGPUBlendState.color(segment, color.toNative(allocator));
        WGPUBlendState.alpha(segment, alpha.toNative(allocator));
        return segment;
    }
}
