package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBlendState;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record BlendState(
    BlendComponent color,
    BlendComponent alpha
) {
    public static BlendStateBuilder builder() {
        return new BlendStateBuilder();
    }

    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUBlendState.allocate(allocator);
        WGPUBlendState.color(segment, color.toNative(allocator));
        WGPUBlendState.alpha(segment, alpha.toNative(allocator));
        return segment;
    }
}
