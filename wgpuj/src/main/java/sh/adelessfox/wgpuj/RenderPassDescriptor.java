package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPURenderPassDescriptor;
import sh.adelessfox.wgpuj.objects.QuerySet;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@WgpuStyle
@Value.Immutable
public interface RenderPassDescriptor extends ObjectDescriptorBase, WgpuStruct {
    List<RenderPassColorAttachment> colorAttachments();

    Optional<RenderPassDepthStencilAttachment> depthStencilAttachment();

    Optional<QuerySet> occlusionQuerySet();

    Optional<RenderPassTimestampWrites> timestampWrites();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPURenderPassDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPURenderPassDescriptor.label(segment), label());
        WgpuUtils.setArray(allocator, segment, WGPURenderPassDescriptor.colorAttachmentCount$offset(), colorAttachments());
        WGPURenderPassDescriptor.depthStencilAttachment(segment, depthStencilAttachment().map(dsa -> dsa.toNative(allocator)).orElse(MemorySegment.NULL));
        WGPURenderPassDescriptor.occlusionQuerySet(segment, occlusionQuerySet().map(QuerySet::segment).orElse(MemorySegment.NULL));
        WGPURenderPassDescriptor.timestampWrites(segment, timestampWrites().map(tw -> tw.toNative(allocator)).orElse(MemorySegment.NULL));
    }
}
