package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPURenderPipelineDescriptor;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@Value.Builder
public record RenderPipelineDescriptor(
    Optional<String> label,
    Optional<PipelineLayout> layout,
    VertexState vertex,
    PrimitiveState primitive,
    Optional<DepthStencilState> depthStencil,
    MultisampleState multisample,
    Optional<FragmentState> fragment
) implements WgpuStruct {
    public static RenderPipelineDescriptorBuilder builder() {
        return new RenderPipelineDescriptorBuilder();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPURenderPipelineDescriptor.allocate(allocator);
        WgpuUtils.setString(allocator, WGPURenderPipelineDescriptor.label(segment), label);
        WGPURenderPipelineDescriptor.layout(segment, layout.map(PipelineLayout::segment).orElse(MemorySegment.NULL));
        WGPURenderPipelineDescriptor.vertex(segment, vertex.toNative(allocator));
        WGPURenderPipelineDescriptor.primitive(segment, primitive.toNative(allocator));
        WGPURenderPipelineDescriptor.depthStencil(segment, depthStencil.map(ds -> ds.toNative(allocator)).orElse(MemorySegment.NULL));
        WGPURenderPipelineDescriptor.multisample(segment, multisample.toNative(allocator));
        WGPURenderPipelineDescriptor.fragment(segment, fragment.map(f -> f.toNative(allocator)).orElse(MemorySegment.NULL));
        return segment;
    }
}
