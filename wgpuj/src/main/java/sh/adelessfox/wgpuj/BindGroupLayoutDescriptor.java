package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupLayoutDescriptor;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@Value.Builder
public record BindGroupLayoutDescriptor(
    Optional<String> label,
    List<BindGroupLayoutEntry> entries
) implements WgpuStruct {
    public BindGroupLayoutDescriptor {
        entries = List.copyOf(entries);
    }

    public static BindGroupLayoutDescriptorBuilder builder() {
        return new BindGroupLayoutDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUBindGroupLayoutDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        throw new UnsupportedOperationException();
    }
}
