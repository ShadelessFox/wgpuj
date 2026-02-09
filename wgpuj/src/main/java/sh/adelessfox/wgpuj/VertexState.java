package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUVertexState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;

@WgpuStyle
@Value.Immutable
public interface VertexState extends ProgrammableStage, WgpuStruct {
    List<VertexBufferLayout> buffers();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUVertexState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUVertexState.module(segment, module().segment());
        entryPoint().ifPresent(ep -> WgpuUtils.setString(allocator, WGPUVertexState.entryPoint(segment), ep));
        WgpuUtils.setArray(allocator, segment, WGPUVertexState.constantCount$offset(), constants());
        WgpuUtils.setArray(allocator, segment, WGPUVertexState.bufferCount$offset(), buffers());
    }
}