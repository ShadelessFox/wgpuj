package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUConstantEntry;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface ConstantEntry extends WgpuStruct {
    String key();

    double value();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUConstantEntry.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUConstantEntry.key(segment), key());
        WGPUConstantEntry.value(segment, value());
    }
}
