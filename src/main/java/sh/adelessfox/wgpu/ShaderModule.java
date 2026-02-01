package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuObject;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuShaderModuleRelease;

public record ShaderModule(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpuShaderModuleRelease(segment);
    }
}
