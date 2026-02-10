package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUComputePassDescriptor;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@WgpuStyle
@Value.Immutable
public interface ComputePassDescriptor extends ObjectDescriptorBase, WgpuStruct {
    Optional<ComputePassTimestampWrites> timestampWrites();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUComputePassDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label().ifPresent(x -> WgpuUtils.setString(allocator, WGPUComputePassDescriptor.label(segment), x));
        timestampWrites().ifPresent(x -> WGPUComputePassDescriptor.timestampWrites(segment, x.toNative(allocator)));
    }
}
