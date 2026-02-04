package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBufferDescriptor;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;
import java.util.Set;

@Value.Builder
public record BufferDescriptor(
    Optional<String> label,
    long size,
    Set<BufferUsage> usages,
    boolean mappedAtCreation
) implements WgpuStruct {
    public BufferDescriptor {
        usages = Set.copyOf(usages);
    }

    public static BufferDescriptorBuilder builder() {
        return new BufferDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUBufferDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUBufferDescriptor.label(segment), label);
        WGPUBufferDescriptor.usage(segment, WgpuFlags.toNative(usages));
        WGPUBufferDescriptor.size(segment, size);
        WGPUBufferDescriptor.mappedAtCreation(segment, WgpuUtils.toNative(mappedAtCreation));
    }
}
