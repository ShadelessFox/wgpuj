package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUDeviceDescriptor;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
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
    public MemoryLayout nativeLayout() {
        return WGPUDeviceDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUDeviceDescriptor.label(segment), label);
    }
}
