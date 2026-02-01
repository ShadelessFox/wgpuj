package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUCommandEncoderDescriptor;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@Value.Builder
public record CommandEncoderDescriptor(
    Optional<String> label
) implements WgpuStruct {
    public static CommandEncoderDescriptorBuilder builder() {
        return new CommandEncoderDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUCommandEncoderDescriptor.layout();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUCommandEncoderDescriptor.allocate(allocator);
        WgpuUtils.setString(allocator, WGPUCommandEncoderDescriptor.label(segment), label);
        return segment;
    }
}
