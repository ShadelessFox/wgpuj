package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUChainedStruct;
import sh.adelessfox.wgpu_native.WGPUInstanceDescriptor;
import sh.adelessfox.wgpu_native.WGPUInstanceExtras;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUSType_InstanceExtras;

@WgpuStyle
@Value.Immutable
public interface InstanceDescriptor extends WgpuStruct {
    Set<InstanceFlag> flags();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUInstanceDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        if (!flags().isEmpty()) {
            var extras = WGPUInstanceExtras.allocate(allocator);
            WGPUChainedStruct.sType(WGPUInstanceExtras.chain(extras), WGPUSType_InstanceExtras());
            WGPUInstanceExtras.flags(extras, WgpuFlags.toNative(flags()));

            WGPUInstanceDescriptor.nextInChain(segment, extras);
        }
    }
}
