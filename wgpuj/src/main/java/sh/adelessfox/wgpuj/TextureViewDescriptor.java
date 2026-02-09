package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTextureViewDescriptor;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

@WgpuStyle
@Value.Immutable
public interface TextureViewDescriptor extends ObjectDescriptorBase, WgpuStruct {
    Optional<TextureFormat> format();

    Optional<TextureViewDimension> dimension();

    Set<TextureUsage> usage();

    default TextureAspect aspect() {
        return TextureAspect.ALL;
    }

    default int baseMipLevel() {
        return 0;
    }

    OptionalInt mipLevelCount();

    default int baseArrayLayer() {
        return 0;
    }

    OptionalInt arrayLayerCount();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUTextureViewDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label().ifPresent(x -> WgpuUtils.setString(allocator, WGPUTextureViewDescriptor.label(segment), x));
        WGPUTextureViewDescriptor.format(segment, format().map(TextureFormat::value).orElse(WGPUTextureFormat_Undefined()));
        WGPUTextureViewDescriptor.dimension(segment, dimension().map(TextureViewDimension::value).orElse(WGPUTextureViewDimension_Undefined()));
        WGPUTextureViewDescriptor.baseMipLevel(segment, baseMipLevel());
        WGPUTextureViewDescriptor.mipLevelCount(segment, mipLevelCount().orElse(WGPU_MIP_LEVEL_COUNT_UNDEFINED()));
        WGPUTextureViewDescriptor.baseArrayLayer(segment, baseArrayLayer());
        WGPUTextureViewDescriptor.arrayLayerCount(segment, arrayLayerCount().orElse(WGPU_ARRAY_LAYER_COUNT_UNDEFINED()));
        WGPUTextureViewDescriptor.aspect(segment, aspect().value());
        WGPUTextureViewDescriptor.usage(segment, WgpuFlags.toNative(usage()));
    }
}
