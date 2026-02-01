package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuTextureCreateView;
import static sh.adelessfox.wgpu_native.wgpu_h.wgpuTextureRelease;

public record Texture(MemorySegment segment) implements WgpuObject {
    public TextureView createView() {
        return new TextureView(wgpuTextureCreateView(segment, MemorySegment.NULL));
    }

    public TextureView createView(TextureViewDescriptor descriptor) {
        try (var arena = Arena.ofConfined()) {
            return new TextureView(wgpuTextureCreateView(segment, descriptor.toNative(arena)));
        }
    }

    @Override
    public void close() {
        wgpuTextureRelease(segment);
    }
}
