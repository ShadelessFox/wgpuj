package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUSurfaceDescriptor;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface SurfaceDescriptor extends ObjectDescriptorBase, WgpuStruct {
    SurfaceSource source();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUSurfaceDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label().ifPresent(x -> WgpuUtils.setString(allocator, WGPUSurfaceDescriptor.label(segment), x));
        WGPUSurfaceDescriptor.nextInChain(segment, source().toNative(allocator));
    }
}
