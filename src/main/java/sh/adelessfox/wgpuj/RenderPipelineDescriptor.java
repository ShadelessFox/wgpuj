package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPURenderPipelineDescriptor;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
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
    public MemoryLayout nativeLayout() {
        return WGPURenderPipelineDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPURenderPipelineDescriptor.label(segment), label);
        WGPURenderPipelineDescriptor.layout(segment, layout.map(PipelineLayout::segment).orElse(MemorySegment.NULL));
        vertex.toNative(allocator, WGPURenderPipelineDescriptor.vertex(segment));
        primitive.toNative(allocator, WGPURenderPipelineDescriptor.primitive(segment));
        WGPURenderPipelineDescriptor.depthStencil(segment, depthStencil.map(ds -> ds.toNative(allocator)).orElse(MemorySegment.NULL));
        multisample.toNative(allocator, WGPURenderPipelineDescriptor.multisample(segment));
        WGPURenderPipelineDescriptor.fragment(segment, fragment.map(f -> f.toNative(allocator)).orElse(MemorySegment.NULL));
    }
}
