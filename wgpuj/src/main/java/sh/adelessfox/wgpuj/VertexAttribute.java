package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUVertexAttribute;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record VertexAttribute(
    VertexFormat format,
    long offset,
    int shaderLocation
) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUVertexAttribute.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUVertexAttribute.format(segment, format.value());
        WGPUVertexAttribute.offset(segment, offset);
        WGPUVertexAttribute.shaderLocation(segment, shaderLocation);
    }
}
