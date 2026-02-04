package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUOrigin3D;
import sh.adelessfox.wgpuj.util.WgpuStruct;

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
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUOrigin3D.x(segment, x);
        WGPUOrigin3D.y(segment, y);
        WGPUOrigin3D.z(segment, z);
    }
}
