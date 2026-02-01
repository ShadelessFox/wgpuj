package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUColorTargetState;

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
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUColorTargetState.allocate(allocator);
        WGPUColorTargetState.format(segment, format.value());
        blend.ifPresent(blend -> WGPUColorTargetState.blend(segment, blend.toNative(allocator)));
        WGPUColorTargetState.writeMask(segment, WgpuUtils.toNative(writeMask));
        return segment;
    }
}
