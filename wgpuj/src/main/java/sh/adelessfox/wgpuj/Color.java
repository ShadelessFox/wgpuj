package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUColor;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record Color(
    double r,
    double g,
    double b,
    double a
) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUColor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUColor.r(segment, r);
        WGPUColor.g(segment, g);
        WGPUColor.b(segment, b);
        WGPUColor.a(segment, a);
    }
}
