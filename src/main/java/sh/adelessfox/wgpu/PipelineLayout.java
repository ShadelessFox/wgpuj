package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuPipelineLayoutRelease;

public record PipelineLayout(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() throws Exception {
        wgpuPipelineLayoutRelease(segment);
    }
}
