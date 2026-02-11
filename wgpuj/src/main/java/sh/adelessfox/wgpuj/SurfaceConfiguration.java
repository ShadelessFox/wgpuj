package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUSurfaceConfiguration;
import sh.adelessfox.wgpuj.objects.Device;
import sh.adelessfox.wgpuj.objects.Surface;
import sh.adelessfox.wgpuj.objects.TextureView;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Set;

/**
 * Options to {@link Surface#configure(SurfaceConfiguration)} for defining how
 * a {@link Surface} will be rendered to and presented to the user.
 */
@WgpuStyle
@Value.Immutable
public interface SurfaceConfiguration extends WgpuStruct {
    /**
     * The {@link Device} to use to render to surface's textures.
     */
    Device device();

    /**
     * The {@link TextureFormat} of the surface's textures.
     */
    TextureFormat format();

    /**
     * The {@link TextureUsage} of the surface's textures.
     */
    Set<TextureUsage> usage();

    /**
     * The width of the surface's textures.
     */
    int width();

    /**
     * The height of the surface's textures.
     */
    int height();

    /**
     * The additional {@link TextureFormat} for {@link TextureView}
     * format reinterpretation of the surface's textures.
     */
    List<TextureFormat> viewFormats();

    /**
     * How the surface's frames will be composited on the screen.
     */
    default CompositeAlphaMode alphaMode() {
        return CompositeAlphaMode.AUTO;
    }

    /**
     * When and in which order the surface's frames will be shown on the screen.
     */
    default PresentMode presentMode() {
        return PresentMode.FIFO;
    }

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUSurfaceConfiguration.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUSurfaceConfiguration.device(segment, device().segment());
        WGPUSurfaceConfiguration.format(segment, format().value());
        WGPUSurfaceConfiguration.usage(segment, WgpuFlags.toNative(usage()));
        WGPUSurfaceConfiguration.width(segment, width());
        WGPUSurfaceConfiguration.height(segment, height());
        WgpuUtils.setArray(allocator, segment, viewFormats(), WGPUSurfaceConfiguration::viewFormatCount, WGPUSurfaceConfiguration::viewFormats);
        WGPUSurfaceConfiguration.alphaMode(segment, alphaMode().value());
        WGPUSurfaceConfiguration.presentMode(segment, presentMode().value());
    }
}
