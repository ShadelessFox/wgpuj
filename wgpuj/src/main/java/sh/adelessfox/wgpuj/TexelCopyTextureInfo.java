package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTexelCopyTextureInfo;
import sh.adelessfox.wgpuj.objects.Texture;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface TexelCopyTextureInfo extends WgpuStruct {
    Texture texture();

    default int mipLevel() {
        return 0;
    }

    default Origin3D origin() {
        return ImmutableOrigin3D.of();
    }

    default TextureAspect aspect() {
        return TextureAspect.ALL;
    }

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUTexelCopyTextureInfo.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUTexelCopyTextureInfo.texture(segment, texture().segment());
        WGPUTexelCopyTextureInfo.mipLevel(segment, mipLevel());
        origin().toNative(allocator, WGPUTexelCopyTextureInfo.origin(segment));
        WGPUTexelCopyTextureInfo.aspect(segment, aspect().value());
    }
}
