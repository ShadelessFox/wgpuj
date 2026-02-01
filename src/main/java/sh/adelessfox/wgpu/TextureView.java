package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuTextureViewRelease;

public record TextureView(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() {
        wgpuTextureViewRelease(segment);
    }
}
