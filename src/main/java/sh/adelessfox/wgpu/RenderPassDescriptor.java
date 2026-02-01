package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPURenderPassDescriptor;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@Value.Builder
public record RenderPassDescriptor(
    Optional<String> label,
    List<RenderPassColorAttachment> colorAttachments,
    Optional<RenderPassDepthStencilAttachment> depthStencilAttachment,
    Optional<QuerySet> occlusionQuerySet,
    Optional<RenderPassTimestampWrites> timestampWrites
) implements WgpuStruct {
    public static RenderPassDescriptorBuilder builder() {
        return new RenderPassDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPURenderPassDescriptor.layout();
    }

    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPURenderPassDescriptor.allocate(allocator);
        WgpuUtils.setString(allocator, WGPURenderPassDescriptor.label(segment), label);
        WgpuUtils.setArray(allocator, segment, WGPURenderPassDescriptor.colorAttachmentCount$offset(), colorAttachments);
        WGPURenderPassDescriptor.depthStencilAttachment(segment, depthStencilAttachment.map(dsa -> dsa.toNative(allocator)).orElse(MemorySegment.NULL));
        WGPURenderPassDescriptor.occlusionQuerySet(segment, occlusionQuerySet.map(QuerySet::segment).orElse(MemorySegment.NULL));
        WGPURenderPassDescriptor.timestampWrites(segment, timestampWrites.map(tw -> tw.toNative(allocator)).orElse(MemorySegment.NULL));
        return segment;
    }
}
