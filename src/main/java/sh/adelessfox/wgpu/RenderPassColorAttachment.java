package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPURenderPassColorAttachment;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;
import java.util.OptionalInt;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

@Value.Builder
public record RenderPassColorAttachment(
    TextureView view,
    OptionalInt depthSlice,
    Optional<TextureView> resolveTarget,
    LoadOp<Color> load,
    StoreOp store
) {
    public static RenderPassColorAttachmentBuilder builder() {
        return new RenderPassColorAttachmentBuilder();
    }

    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPURenderPassColorAttachment.allocate(allocator);
        WGPURenderPassColorAttachment.view(segment, view.segment());
        WGPURenderPassColorAttachment.depthSlice(segment, depthSlice.orElse(WGPU_DEPTH_SLICE_UNDEFINED()));
        WGPURenderPassColorAttachment.resolveTarget(segment, resolveTarget.map(TextureView::segment).orElse(MemorySegment.NULL));
        WGPURenderPassColorAttachment.loadOp(segment, switch (load) {
            case LoadOp.Clear<Color>(var color) -> {
                WGPURenderPassColorAttachment.clearValue(segment, color.toNative(allocator));
                yield WGPULoadOp_Clear();
            }
            case LoadOp.Load<Color> _ -> WGPULoadOp_Load();
            case LoadOp.DontCare<Color> _ -> WGPULoadOp_Undefined();
        });
        WGPURenderPassColorAttachment.storeOp(segment, store.value());
        return segment;
    }
}
