package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUColorTargetState;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;
import java.util.Set;

@Value.Builder
public record ColorTargetState(
    TextureFormat format,
    Optional<BlendState> blend,
    Set<ColorWrites> writeMask
) implements WgpuStruct {
    public ColorTargetState {
        writeMask = Set.copyOf(writeMask);
    }

    public static ColorTargetStateBuilder builder() {
        return new ColorTargetStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUColorTargetState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUColorTargetState.format(segment, format.value());
        blend.ifPresent(blend -> WGPUColorTargetState.blend(segment, blend.toNative(allocator)));
        WGPUColorTargetState.writeMask(segment, WgpuFlags.toNative(writeMask));
    }
}
