package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuFlags;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUChainedStruct;
import sh.adelessfox.wgpu_native.WGPUInstanceDescriptor;
import sh.adelessfox.wgpu_native.WGPUInstanceExtras;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUSType_InstanceExtras;

@Value.Builder
public record InstanceDescriptor(
    Set<InstanceFlag> flags
) implements WgpuStruct {
    public InstanceDescriptor {
        flags = Set.copyOf(flags);
    }

    public static InstanceDescriptorBuilder builder() {
        return new InstanceDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUInstanceDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        var extras = WGPUInstanceExtras.allocate(allocator);
        WGPUChainedStruct.sType(WGPUInstanceExtras.chain(extras), WGPUSType_InstanceExtras());
        WGPUInstanceExtras.flags(extras, WgpuFlags.toNative(flags));

        WGPUInstanceDescriptor.nextInChain(segment, extras);
    }
}
