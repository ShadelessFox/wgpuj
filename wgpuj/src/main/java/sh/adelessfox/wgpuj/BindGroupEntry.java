package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupEntry;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_WHOLE_SIZE;

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
        WGPUBindGroupEntry.binding(segment, binding);
        switch (resource) {
            case BindingResource.Buffer(var buffer, var offset, var size) -> {
                WGPUBindGroupEntry.buffer(segment, buffer.segment());
                WGPUBindGroupEntry.offset(segment, offset);
                WGPUBindGroupEntry.size(segment, size.orElse(WGPU_WHOLE_SIZE()));
            }
            case BindingResource.Sampler(var sampler) -> {
                WGPUBindGroupEntry.sampler(segment, sampler.segment());
            }
            case BindingResource.TextureView(var view) -> {
                WGPUBindGroupEntry.textureView(segment, view.segment());
            }
        }
    }
}
