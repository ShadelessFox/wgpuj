package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUVertexState;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@Value.Builder
public record VertexState(
    ShaderModule module,
    Optional<String> entryPoint,
    List<ConstantEntry> constants,
    List<VertexBufferLayout> buffers
) implements WgpuStruct {
    public static VertexStateBuilder builder() {
        return new VertexStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUVertexState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUVertexState.module(segment, module.segment());
        entryPoint.ifPresent(ep -> WgpuUtils.setString(allocator, WGPUVertexState.entryPoint(segment), ep));
        WgpuUtils.setArray(allocator, segment, WGPUVertexState.constantCount$offset(), constants);
        WgpuUtils.setArray(allocator, segment, WGPUVertexState.bufferCount$offset(), buffers);
    }
}