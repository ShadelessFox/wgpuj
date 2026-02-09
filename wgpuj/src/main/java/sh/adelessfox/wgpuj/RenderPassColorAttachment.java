package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPURenderPassColorAttachment;
import sh.adelessfox.wgpuj.objects.TextureView;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;
import java.util.OptionalInt;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_DEPTH_SLICE_UNDEFINED;

@WgpuStyle
@Value.Immutable
public interface RenderPassColorAttachment extends WgpuStruct {
    TextureView view();

    OptionalInt depthSlice();

    Optional<TextureView> resolveTarget();

    Operations<Color> ops();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPURenderPassColorAttachment.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPURenderPassColorAttachment.view(segment, view().segment());
        WGPURenderPassColorAttachment.depthSlice(segment, depthSlice().orElse(WGPU_DEPTH_SLICE_UNDEFINED()));
        WGPURenderPassColorAttachment.resolveTarget(segment, resolveTarget().map(TextureView::segment).orElse(MemorySegment.NULL));
        ops().toNative(
            segment,
            WGPURenderPassColorAttachment::loadOp,
            WGPURenderPassColorAttachment::storeOp,
            (s, c) -> c.toNative(allocator, WGPURenderPassColorAttachment.clearValue(s))
        );
    }
}
