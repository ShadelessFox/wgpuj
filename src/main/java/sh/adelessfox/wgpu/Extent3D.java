package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu_native.WGPUExtent3D;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record Extent3D(
    int width,
    int height,
    int depthOrArrayLayers
) {
    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUExtent3D.allocate(allocator);
        WGPUExtent3D.width(segment, width);
        WGPUExtent3D.height(segment, height);
        WGPUExtent3D.depthOrArrayLayers(segment, depthOrArrayLayers);
        return segment;
    }
}
