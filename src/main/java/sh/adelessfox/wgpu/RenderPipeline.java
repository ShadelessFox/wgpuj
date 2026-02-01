package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuRenderPipelineRelease;

public record RenderPipeline(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() {
        wgpuRenderPipelineRelease(segment);
    }
}
