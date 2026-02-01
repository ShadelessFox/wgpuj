package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTexelCopyTextureInfo;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record TexelCopyTextureInfo(
    Texture texture,
    int mipLevel,
    Origin3D origin,
    TextureAspect aspect
) {
    public static TexelCopyTextureInfoBuilder builder() {
        return new TexelCopyTextureInfoBuilder();
    }

    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUTexelCopyTextureInfo.allocate(allocator);
        WGPUTexelCopyTextureInfo.texture(segment, texture.segment());
        WGPUTexelCopyTextureInfo.mipLevel(segment, mipLevel);
        WGPUTexelCopyTextureInfo.origin(segment, origin.toNative(allocator));
        WGPUTexelCopyTextureInfo.aspect(segment, aspect.value());
        return segment;
    }
}
