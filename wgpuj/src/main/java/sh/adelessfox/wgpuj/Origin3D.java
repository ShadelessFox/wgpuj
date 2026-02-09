package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUOrigin3D;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface Origin3D extends WgpuStruct {
    default int x() {
        return 0;
    }

    default int y() {
        return 0;
    }

    default int z() {
        return 0;
    }

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUOrigin3D.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUOrigin3D.x(segment, x());
        WGPUOrigin3D.y(segment, y());
        WGPUOrigin3D.z(segment, z());
    }
}
