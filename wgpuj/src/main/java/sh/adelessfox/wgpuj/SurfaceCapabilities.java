package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUSurfaceCapabilities;
import sh.adelessfox.wgpuj.objects.Adapter;
import sh.adelessfox.wgpuj.objects.Surface;
import sh.adelessfox.wgpuj.util.*;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Set;

/**
 * Filled by {@link Surface#getCapabilities(Adapter)} with what's supported
 * for {@link Surface#configure(SurfaceConfiguration)} for a pair
 * of {@link Surface} and {@link Adapter}.
 */
@WgpuStyle
@Value.Immutable
public interface SurfaceCapabilities extends WgpuStruct {
    static SurfaceCapabilities ofNative(MemorySegment segment) {
        var usages = WgpuFlags.ofNative(WGPUSurfaceCapabilities.usages(segment), TextureUsage.class);
        var formats = WgpuUtils.getArray(
            WGPUSurfaceCapabilities.formatCount(segment),
            WGPUSurfaceCapabilities.formats(segment),
            WgpuEnum.LAYOUT,
            entry -> WgpuEnum.ofNative(entry, TextureFormat.class));
        var presentModes = WgpuUtils.getArray(
            WGPUSurfaceCapabilities.presentModeCount(segment),
            WGPUSurfaceCapabilities.presentModes(segment),
            WgpuEnum.LAYOUT,
            entry -> WgpuEnum.ofNative(entry, PresentMode.class));
        var alphaModes = WgpuUtils.getArray(
            WGPUSurfaceCapabilities.alphaModeCount(segment),
            WGPUSurfaceCapabilities.alphaModes(segment),
            WgpuEnum.LAYOUT,
            entry -> WgpuEnum.ofNative(entry, CompositeAlphaMode.class));
        return ImmutableSurfaceCapabilities.builder()
            .usages(usages)
            .formats(formats)
            .presentModes(presentModes)
            .alphaModes(alphaModes)
            .build();
    }

    /**
     * The bit set of supported {@link TextureUsage} bits.
     * Guaranteed to contain @ref WGPUTextureUsage_RenderAttachment.
     */
    Set<TextureUsage> usages();

    /**
     * A list of supported {@link TextureFormat} values, in order of preference.
     */
    List<TextureFormat> formats();

    /**
     * A list of supported {@link PresentMode} values.
     * <p>
     * Guaranteed to contain {@link PresentMode#FIFO}.
     */
    List<PresentMode> presentModes();

    /**
     * A list of supported {@link CompositeAlphaMode} values.
     * <p>
     * {@link CompositeAlphaMode#AUTO} will be an alias for
     * the first element and will never be present in this array.
     */
    List<CompositeAlphaMode> alphaModes();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUSurfaceCapabilities.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        throw new UnsupportedOperationException();
    }
}
