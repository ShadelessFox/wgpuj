package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuFlags;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUTextureDescriptor;

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
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUTextureDescriptor.allocate(allocator);
        label.ifPresent(l -> WgpuUtils.setString(allocator, WGPUTextureDescriptor.label(segment), l));
        WGPUTextureDescriptor.size(segment, size.toNative(allocator));
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
        return segment;
    }
}
