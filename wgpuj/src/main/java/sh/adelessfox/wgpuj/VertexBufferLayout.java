package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUVertexBufferLayout;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;

@WgpuStyle
@Value.Immutable
public interface VertexBufferLayout extends WgpuStruct {
    long arrayStride();

    default VertexStepMode stepMode() {
        return VertexStepMode.VERTEX;
    }

    List<VertexAttribute> attributes();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUVertexBufferLayout.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUVertexBufferLayout.stepMode(segment, stepMode().value());
        WGPUVertexBufferLayout.arrayStride(segment, arrayStride());
        WgpuUtils.setArray(allocator, segment, attributes(), WGPUVertexBufferLayout::attributeCount, WGPUVertexBufferLayout::attributes);
    }
}
