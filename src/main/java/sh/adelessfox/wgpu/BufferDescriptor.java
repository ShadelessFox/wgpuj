package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUBufferDescriptor;

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
) {
    public BufferDescriptor {
        usages = Set.copyOf(usages);
    }

    public static BufferDescriptorBuilder builder() {
        return new BufferDescriptorBuilder();
    }

    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUBufferDescriptor.allocate(allocator);
        WgpuUtils.setString(allocator, WGPUBufferDescriptor.label(segment), label);
        WGPUBufferDescriptor.usage(segment, WgpuUtils.toNative(usages));
        WGPUBufferDescriptor.size(segment, size);
        WGPUBufferDescriptor.mappedAtCreation(segment, WgpuUtils.toNative(mappedAtCreation));
        return segment;
    }
}
