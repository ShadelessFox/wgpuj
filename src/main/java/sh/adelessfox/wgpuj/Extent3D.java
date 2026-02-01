package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUExtent3D;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record Extent3D(
    int width,
    int height,
    int depthOrArrayLayers
) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUExtent3D.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUExtent3D.width(segment, width);
        WGPUExtent3D.height(segment, height);
        WGPUExtent3D.depthOrArrayLayers(segment, depthOrArrayLayers);
    }
}
