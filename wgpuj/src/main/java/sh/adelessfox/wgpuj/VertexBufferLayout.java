package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUVertexBufferLayout;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;

@Value.Builder
public record VertexBufferLayout(
    VertexStepMode stepMode,
    long arrayStride,
    List<VertexAttribute> attributes
) implements WgpuStruct {
    public VertexBufferLayout {
        attributes = List.copyOf(attributes);
    }

    public static VertexBufferLayoutBuilder builder() {
        return new VertexBufferLayoutBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUVertexBufferLayout.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUVertexBufferLayout.stepMode(segment, stepMode.value());
        WGPUVertexBufferLayout.arrayStride(segment, arrayStride);
        WgpuUtils.setArray(allocator, segment, WGPUVertexBufferLayout.attributeCount$offset(), attributes);
    }
}
