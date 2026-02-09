package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUPipelineLayoutDescriptor;
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
public interface PipelineLayoutDescriptor extends ObjectDescriptorBase, WgpuStruct {
    List<BindGroupLayout> bindGroupLayouts();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUPipelineLayoutDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label().ifPresent(x -> WgpuUtils.setString(allocator, WGPUPipelineLayoutDescriptor.label(segment), x));
        WgpuUtils.setArray(allocator, segment, bindGroupLayouts(), WGPUPipelineLayoutDescriptor::bindGroupLayoutCount, WGPUPipelineLayoutDescriptor::bindGroupLayouts);
    }
}
