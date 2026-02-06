package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupEntry;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record BindGroupEntry(
    int binding,
    BindingResource resource
) implements WgpuStruct {
    public static BindGroupEntryBuilder builder() {
        return new BindGroupEntryBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUBindGroupEntry.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        throw new UnsupportedOperationException();
    }
}
