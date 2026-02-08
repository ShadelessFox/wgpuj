package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPURenderPassDepthStencilAttachment;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@Value.Builder
public record RenderPassDepthStencilAttachment(
    TextureView view,
    Optional<Operations<Float>> depthOps,
    Optional<Operations<Integer>> stencilOps
) implements WgpuStruct {
    public static RenderPassDepthStencilAttachmentBuilder builder() {
        return new RenderPassDepthStencilAttachmentBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPURenderPassDepthStencilAttachment.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPURenderPassDepthStencilAttachment.view(segment, view.segment());
        Operations.toNative(
            depthOps,
            segment,
            WGPURenderPassDepthStencilAttachment::depthLoadOp,
            WGPURenderPassDepthStencilAttachment::depthStoreOp,
            WGPURenderPassDepthStencilAttachment::depthClearValue);
        Operations.toNative(
            stencilOps,
            segment,
            WGPURenderPassDepthStencilAttachment::stencilLoadOp,
            WGPURenderPassDepthStencilAttachment::stencilStoreOp,
            WGPURenderPassDepthStencilAttachment::stencilClearValue);
    }
}
