package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTextureDescriptor;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Value.Builder
public record TextureDescriptor(
    Optional<String> label,
    Extent3D size,
    int mipLevelCount,
    int sampleCount,
    TextureDimension dimension,
    TextureFormat format,
    Set<TextureUsage> usages,
    List<TextureFormat> viewFormats
) implements WgpuStruct {
    public TextureDescriptor {
        usages = Set.copyOf(usages);
        viewFormats = List.copyOf(viewFormats);
    }

    public static TextureDescriptorBuilder builder() {
        return new TextureDescriptorBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUTextureDescriptor.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label.ifPresent(l -> WgpuUtils.setString(allocator, WGPUTextureDescriptor.label(segment), l));
        size.toNative(allocator, WGPUTextureDescriptor.size(segment));
        WGPUTextureDescriptor.mipLevelCount(segment, mipLevelCount);
        WGPUTextureDescriptor.sampleCount(segment, sampleCount);
        WGPUTextureDescriptor.dimension(segment, dimension.value());
        WGPUTextureDescriptor.format(segment, format.value());
        WGPUTextureDescriptor.usage(segment, WgpuFlags.toNative(usages));
        if (!viewFormats.isEmpty()) {
            int[] elements = viewFormats.stream()
                .mapToInt(TextureFormat::value)
                .toArray();
            WGPUTextureDescriptor.viewFormatCount(segment, elements.length);
            WGPUTextureDescriptor.viewFormats(segment, allocator.allocateFrom(ValueLayout.JAVA_INT, elements));
        }
    }
}
