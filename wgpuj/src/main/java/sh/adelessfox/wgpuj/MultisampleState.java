package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUMultisampleState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record MultisampleState(
    int count,
    int mask,
    boolean alphaToCoverageEnabled
) implements WgpuStruct {
    public static MultisampleStateBuilder builder() {
        return new MultisampleStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUMultisampleState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUMultisampleState.count(segment, count);
        WGPUMultisampleState.mask(segment, mask);
        WGPUMultisampleState.alphaToCoverageEnabled(segment, WgpuUtils.toNative(alphaToCoverageEnabled));
    }
}
