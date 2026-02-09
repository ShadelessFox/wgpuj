package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUExtent3D;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface Extent3D extends WgpuStruct {
    int width();

    default int height() {
        return 1;
    }

    default int depthOrArrayLayers() {
        return 1;
    }

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUExtent3D.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUExtent3D.width(segment, width());
        WGPUExtent3D.height(segment, height());
        WGPUExtent3D.depthOrArrayLayers(segment, depthOrArrayLayers());
    }
}
