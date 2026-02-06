package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUVertexBufferLayout;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUVertexStepMode_Undefined;

public record VertexBufferLayout(
    Optional<VertexStepMode> stepMode,
    long arrayStride,
    List<VertexAttribute> attributes
) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUVertexBufferLayout.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUVertexBufferLayout.stepMode(segment, stepMode.map(VertexStepMode::value).orElse(WGPUVertexStepMode_Undefined()));
        WGPUVertexBufferLayout.arrayStride(segment, arrayStride);
        WgpuUtils.setArray(allocator, segment, WGPUVertexBufferLayout.attributeCount$offset(), attributes);
    }
}
