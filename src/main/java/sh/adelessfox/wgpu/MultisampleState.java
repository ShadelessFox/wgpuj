package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUMultisampleState;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record MultisampleState(
    int count,
    int mask,
    boolean alphaToCoverageEnabled
) {
    public static MultisampleStateBuilder builder() {
        return new MultisampleStateBuilder();
    }

    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUMultisampleState.allocate(allocator);
        WGPUMultisampleState.count(segment, count);
        WGPUMultisampleState.mask(segment, mask);
        WGPUMultisampleState.alphaToCoverageEnabled(segment, WgpuUtils.toNative(alphaToCoverageEnabled));
        return segment;
    }
}
