package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuShaderModuleRelease;

public record ShaderModule(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() {
        wgpuShaderModuleRelease(segment);
    }
}
