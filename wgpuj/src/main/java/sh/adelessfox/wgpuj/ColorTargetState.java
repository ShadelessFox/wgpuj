package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUColorTargetState;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;
import java.util.Set;

@WgpuStyle
@Value.Immutable
public interface ColorTargetState extends WgpuStruct {
    TextureFormat format();

    Optional<BlendState> blend();

    default Set<ColorWrites> writeMask() {
        return Set.of(ColorWrites.ALL);
    }

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUColorTargetState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUColorTargetState.format(segment, format().value());
        blend().ifPresent(blend -> WGPUColorTargetState.blend(segment, blend.toNative(allocator)));
        WGPUColorTargetState.writeMask(segment, WgpuFlags.toNative(writeMask()));
    }
}
