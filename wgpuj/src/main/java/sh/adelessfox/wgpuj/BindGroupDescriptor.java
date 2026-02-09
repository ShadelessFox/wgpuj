package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupDescriptor;
import sh.adelessfox.wgpuj.objects.BindGroupLayout;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;

@WgpuStyle
@Value.Immutable
public interface BindGroupDescriptor extends ObjectDescriptorBase, WgpuStruct {
    BindGroupLayout layout();

    List<BindGroupEntry> entries();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUBindGroupDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUBindGroupDescriptor.label(segment), label());
        WGPUBindGroupDescriptor.layout(segment, layout().segment());
        WgpuUtils.setArray(allocator, segment, WGPUBindGroupDescriptor.entryCount$offset(), entries());
    }
}
