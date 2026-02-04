package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTextureViewDescriptor;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_ARRAY_LAYER_COUNT_UNDEFINED;
import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_MIP_LEVEL_COUNT_UNDEFINED;

@Value.Builder
public record TextureViewDescriptor(
    Optional<String> label,
    Optional<TextureFormat> format,
    Optional<TextureViewDimension> dimension,
    Set<TextureUsage> usage,
    Optional<TextureAspect> aspect,
    int baseMipLevel,
    OptionalInt mipLevelCount,
    int baseArrayLayer,
    OptionalInt arrayLayerCount
) implements WgpuStruct {
    public TextureViewDescriptor {
        usage = Set.copyOf(usage);
    }

    public static TextureViewDescriptorBuilder builder() {
        return new TextureViewDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUTextureViewDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label.ifPresent(l -> WgpuUtils.setString(allocator, WGPUTextureViewDescriptor.label(segment), l));
        WGPUTextureViewDescriptor.format(segment, format.orElse(TextureFormat.UNDEFINED).value());
        WGPUTextureViewDescriptor.dimension(segment, dimension.orElse(TextureViewDimension.UNDEFINED).value());
        WGPUTextureViewDescriptor.usage(segment, WgpuFlags.toNative(usage));
        WGPUTextureViewDescriptor.aspect(segment, aspect.orElse(TextureAspect.UNDEFINED).value());
        WGPUTextureViewDescriptor.baseMipLevel(segment, baseMipLevel);
        WGPUTextureViewDescriptor.mipLevelCount(segment, mipLevelCount.orElse(WGPU_MIP_LEVEL_COUNT_UNDEFINED()));
        WGPUTextureViewDescriptor.baseArrayLayer(segment, baseArrayLayer);
        WGPUTextureViewDescriptor.arrayLayerCount(segment, arrayLayerCount.orElse(WGPU_ARRAY_LAYER_COUNT_UNDEFINED()));
    }
}
