package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuRenderBundleRelease;

public record RenderBundle(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() throws Exception {
        wgpuRenderBundleRelease(segment);
    }
}
