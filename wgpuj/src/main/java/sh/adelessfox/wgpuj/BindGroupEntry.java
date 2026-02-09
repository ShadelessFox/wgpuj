package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupEntry;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_WHOLE_SIZE;

@WgpuStyle
@Value.Immutable
public interface BindGroupEntry extends WgpuStruct {
    int binding();

    BindingResource resource();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUBindGroupEntry.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUBindGroupEntry.binding(segment, binding());
        switch (resource()) {
            case BindingResource.Buffer buffer -> {
                WGPUBindGroupEntry.buffer(segment, buffer.buffer().segment());
                WGPUBindGroupEntry.offset(segment, buffer.offset());
                WGPUBindGroupEntry.size(segment, buffer.size().orElse(WGPU_WHOLE_SIZE()));
            }
            case BindingResource.Sampler sampler -> {
                WGPUBindGroupEntry.sampler(segment, sampler.sampler().segment());
            }
            case BindingResource.TextureView view -> {
                WGPUBindGroupEntry.textureView(segment, view.view().segment());
            }
        }
    }
}
