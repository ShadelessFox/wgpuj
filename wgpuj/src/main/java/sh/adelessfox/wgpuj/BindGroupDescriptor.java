package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupDescriptor;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@Value.Builder
public record BindGroupDescriptor(
    Optional<String> label,
    BindGroupLayout layout,
    List<BindGroupEntry> entries
) implements WgpuStruct {
    public BindGroupDescriptor {
        entries = List.copyOf(entries);
    }

    public static BindGroupDescriptorBuilder builder() {
        return new BindGroupDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUBindGroupDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUBindGroupDescriptor.label(segment), label);
        WGPUBindGroupDescriptor.layout(segment, layout.segment());
        WgpuUtils.setArray(allocator, segment, WGPUBindGroupDescriptor.entryCount$offset(), entries);
    }
}
