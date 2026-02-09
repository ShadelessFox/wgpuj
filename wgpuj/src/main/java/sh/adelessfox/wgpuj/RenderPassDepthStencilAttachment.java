package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPURenderPassDepthStencilAttachment;
import sh.adelessfox.wgpuj.objects.TextureView;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@WgpuStyle
@Value.Immutable
public interface RenderPassDepthStencilAttachment extends WgpuStruct {
    TextureView view();

    Optional<Operations<Float>> depthOps();

    Optional<Operations<Integer>> stencilOps();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPURenderPassDepthStencilAttachment.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPURenderPassDepthStencilAttachment.view(segment, view().segment());
        depthOps().ifPresent(x -> x.toNative(
            segment,
            WGPURenderPassDepthStencilAttachment::depthLoadOp,
            WGPURenderPassDepthStencilAttachment::depthStoreOp,
            WGPURenderPassDepthStencilAttachment::depthClearValue));
        stencilOps().ifPresent(x -> x.toNative(
            segment,
            WGPURenderPassDepthStencilAttachment::stencilLoadOp,
            WGPURenderPassDepthStencilAttachment::stencilStoreOp,
            WGPURenderPassDepthStencilAttachment::stencilClearValue));
    }
}
