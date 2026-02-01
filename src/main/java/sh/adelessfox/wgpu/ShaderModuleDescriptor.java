package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUShaderModuleDescriptor;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@Value.Builder
public record ShaderModuleDescriptor(
    Optional<String> label,
    ShaderSource source
) implements WgpuStruct {
    public static ShaderModuleDescriptorBuilder builder() {
        return new ShaderModuleDescriptorBuilder();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUShaderModuleDescriptor.allocate(allocator);
        WgpuUtils.setString(allocator, WGPUShaderModuleDescriptor.label(segment), label);
        WGPUShaderModuleDescriptor.nextInChain(segment, source.toNative(allocator));
        return segment;
    }
}
