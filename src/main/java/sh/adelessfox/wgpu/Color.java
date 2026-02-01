package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUColor;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record Color(
    double r,
    double g,
    double b,
    double a
) implements WgpuStruct {
    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = allocator.allocate(WGPUColor.layout());
        WGPUColor.r(segment, r);
        WGPUColor.g(segment, g);
        WGPUColor.b(segment, b);
        WGPUColor.a(segment, a);
        return segment;
    }
}
