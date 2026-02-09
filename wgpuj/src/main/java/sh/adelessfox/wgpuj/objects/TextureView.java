package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuTextureViewRelease;

public record TextureView(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpuTextureViewRelease(segment);
    }
}
