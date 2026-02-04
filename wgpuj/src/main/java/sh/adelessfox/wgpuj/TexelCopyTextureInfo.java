package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTexelCopyTextureInfo;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record TexelCopyTextureInfo(
    Texture texture,
    int mipLevel,
    Origin3D origin,
    TextureAspect aspect
) implements WgpuStruct {
    public static TexelCopyTextureInfoBuilder builder() {
        return new TexelCopyTextureInfoBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUTexelCopyTextureInfo.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUTexelCopyTextureInfo.texture(segment, texture.segment());
        WGPUTexelCopyTextureInfo.mipLevel(segment, mipLevel);
        origin.toNative(allocator, WGPUTexelCopyTextureInfo.origin(segment));
        WGPUTexelCopyTextureInfo.aspect(segment, aspect.value());
    }
}
