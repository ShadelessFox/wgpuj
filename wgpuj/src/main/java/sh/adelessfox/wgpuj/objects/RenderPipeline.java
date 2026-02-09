package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuRenderPipelineRelease;

public record RenderPipeline(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpuRenderPipelineRelease(segment);
    }
}
