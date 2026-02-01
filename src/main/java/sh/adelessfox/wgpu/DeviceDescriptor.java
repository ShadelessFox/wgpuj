package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUDeviceDescriptor;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@Value.Builder
public record DeviceDescriptor(
    Optional<String> label
) implements WgpuStruct {
    public static DeviceDescriptorBuilder builder() {
        return new DeviceDescriptorBuilder();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUDeviceDescriptor.allocate(allocator);
        WgpuUtils.setString(allocator, WGPUDeviceDescriptor.label(segment), label);
        return segment;
    }
}
