package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpuj.TextureDimension;
import sh.adelessfox.wgpuj.TextureFormat;
import sh.adelessfox.wgpuj.TextureUsage;
import sh.adelessfox.wgpuj.TextureViewDescriptor;
import sh.adelessfox.wgpuj.util.WgpuEnum;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuObject;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Texture(MemorySegment segment) implements WgpuObject {
    public TextureView createView() {
        return new TextureView(wgpuTextureCreateView(segment, MemorySegment.NULL));
    }

    public TextureView createView(TextureViewDescriptor descriptor) {
        try (var arena = Arena.ofConfined()) {
            return new TextureView(wgpuTextureCreateView(segment, descriptor.toNative(arena)));
        }
    }

    public int getDepthOrArrayLayers() {
        return wgpuTextureGetDepthOrArrayLayers(segment);
    }

    public TextureDimension getDimension() {
        return WgpuEnum.valueOf(wgpuTextureGetDimension(segment), TextureDimension.class);
    }

    public TextureFormat getFormat() {
        return WgpuEnum.valueOf(wgpuTextureGetFormat(segment), TextureFormat.class);
    }

    public int getHeight() {
        return wgpuTextureGetHeight(segment);
    }

    public int getMipLevelCount() {
        return wgpuTextureGetMipLevelCount(segment);
    }

    public int getSampleCount() {
        return wgpuTextureGetSampleCount(segment);
    }

    public Set<TextureUsage> getUsage() {
        return WgpuFlags.setOf(wgpuTextureGetUsage(segment), TextureUsage.class);
    }

    public int getWidth() {
        return wgpuTextureGetWidth(segment);
    }

    public void setLabel(String label) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuTextureSetLabel(segment, WgpuUtils.toNative(arena, label));
        }
    }

    @Override
    public void close() {
        wgpuTextureRelease(segment);
    }
}
