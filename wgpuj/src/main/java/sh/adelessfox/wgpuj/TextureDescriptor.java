package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTextureDescriptor;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.util.List;
import java.util.Set;

@WgpuStyle
@Value.Immutable
public interface TextureDescriptor extends ObjectDescriptorBase, WgpuStruct {
    Extent3D size();

    default int mipLevelCount() {
        return 1;
    }

    default int sampleCount() {
        return 1;
    }

    default TextureDimension dimension() {
        return TextureDimension.D2;
    }

    TextureFormat format();

    Set<TextureUsage> usages();

    List<TextureFormat> viewFormats();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUTextureDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label().ifPresent(l -> WgpuUtils.setString(allocator, WGPUTextureDescriptor.label(segment), l));
        size().toNative(allocator, WGPUTextureDescriptor.size(segment));
        WGPUTextureDescriptor.mipLevelCount(segment, mipLevelCount());
        WGPUTextureDescriptor.sampleCount(segment, sampleCount());
        WGPUTextureDescriptor.dimension(segment, dimension().value());
        WGPUTextureDescriptor.format(segment, format().value());
        WGPUTextureDescriptor.usage(segment, WgpuFlags.toNative(usages()));
        if (!viewFormats().isEmpty()) {
            int[] elements = viewFormats().stream()
                .mapToInt(TextureFormat::value)
                .toArray();
            WGPUTextureDescriptor.viewFormatCount(segment, elements.length);
            WGPUTextureDescriptor.viewFormats(segment, allocator.allocateFrom(ValueLayout.JAVA_INT, elements));
        }
    }
}
