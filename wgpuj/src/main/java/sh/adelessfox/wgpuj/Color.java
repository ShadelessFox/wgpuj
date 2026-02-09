package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUColor;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface Color extends WgpuStruct {
    double r();

    double g();

    double b();

    double a();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUColor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUColor.r(segment, r());
        WGPUColor.g(segment, g());
        WGPUColor.b(segment, b());
        WGPUColor.a(segment, a());
    }
}
