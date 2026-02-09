package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUPipelineLayoutDescriptor;
import sh.adelessfox.wgpuj.objects.BindGroupLayout;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@Value.Builder
public record PipelineLayoutDescriptor(
    Optional<String> label,
    List<BindGroupLayout> bindGroupLayouts
) implements WgpuStruct {
    public PipelineLayoutDescriptor {
        bindGroupLayouts = List.copyOf(bindGroupLayouts);
    }

    public static PipelineLayoutDescriptorBuilder builder() {
        return new PipelineLayoutDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUPipelineLayoutDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUPipelineLayoutDescriptor.label(segment), label);
        WgpuUtils.setArray(allocator, segment, WGPUPipelineLayoutDescriptor.bindGroupLayoutCount$offset(), bindGroupLayouts);
    }
}
