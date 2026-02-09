package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUMultisampleState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface MultisampleState extends WgpuStruct {
    default int count() {
        return 1;
    }

    default int mask() {
        return 0xFFFFFFFF;
    }

    default boolean alphaToCoverageEnabled() {
        return false;
    }

    @Override
    default MemoryLayout nativeLayout() {
        return WGPUMultisampleState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUMultisampleState.count(segment, count());
        WGPUMultisampleState.mask(segment, mask());
        WGPUMultisampleState.alphaToCoverageEnabled(segment, WgpuUtils.toNative(alphaToCoverageEnabled()));
    }
}
