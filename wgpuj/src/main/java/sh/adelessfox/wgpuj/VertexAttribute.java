package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUVertexAttribute;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface VertexAttribute extends WgpuStruct {
    VertexFormat format();

    long offset();

    int shaderLocation();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUVertexAttribute.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUVertexAttribute.format(segment, format().value());
        WGPUVertexAttribute.offset(segment, offset());
        WGPUVertexAttribute.shaderLocation(segment, shaderLocation());
    }
}
