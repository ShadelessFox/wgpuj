package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUOrigin3D;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record Origin3D(
    int x,
    int y,
    int z
) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUOrigin3D.layout();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUOrigin3D.allocate(allocator);
        WGPUOrigin3D.x(segment, x);
        WGPUOrigin3D.y(segment, y);
        WGPUOrigin3D.z(segment, z);
        return segment;
    }
}
