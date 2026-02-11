package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUSurfaceTexture;
import sh.adelessfox.wgpuj.objects.Surface;
import sh.adelessfox.wgpuj.objects.Texture;
import sh.adelessfox.wgpuj.util.WgpuEnum;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

/**
 * Queried each frame from a {@link Surface} to get
 * a {@link Texture} to render to along with some metadata.
 */
@WgpuStyle
@Value.Immutable
public interface SurfaceTexture extends WgpuStruct {
    static SurfaceTexture ofNative(MemorySegment segment) {
        var texture = new Texture(WGPUSurfaceTexture.texture(segment));
        var status = WgpuEnum.ofNative(WGPUSurfaceTexture.status(segment), SurfaceGetCurrentTextureStatus.class);
        return ImmutableSurfaceTexture.builder()
            .texture(texture)
            .status(status)
            .build();
    }

    /**
     * The {@link Texture} representing the frame that will be shown on the surface.
     */
    Texture texture();

    /**
     * Whether the call to `::` succeeded and a hint as to why it might not have.
     */
    SurfaceGetCurrentTextureStatus status();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUSurfaceTexture.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUSurfaceTexture.texture(segment, texture().segment());
        WGPUSurfaceTexture.status(segment, status().value());
    }
}
