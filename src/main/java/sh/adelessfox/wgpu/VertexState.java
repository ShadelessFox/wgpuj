package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUConstantEntry;
import sh.adelessfox.wgpu_native.WGPUVertexBufferLayout;
import sh.adelessfox.wgpu_native.WGPUVertexState;

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
) {
    public static VertexStateBuilder builder() {
        return new VertexStateBuilder();
    }

    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUVertexState.allocate(allocator);
        WGPUVertexState.module(segment, module.segment());
        entryPoint.ifPresent(ep -> WgpuUtils.setString(allocator, WGPUVertexState.entryPoint(segment), ep));
        WgpuUtils.setArray(allocator, segment, WGPUVertexState.constantCount$offset(), WGPUConstantEntry.layout(), constants, ConstantEntry::toNative);
        WgpuUtils.setArray(allocator, segment, WGPUVertexState.bufferCount$offset(), WGPUVertexBufferLayout.layout(), buffers, VertexBufferLayout::toNative);
        return segment;
    }
}