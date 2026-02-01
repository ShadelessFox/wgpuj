package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuBindGroupRelease;

public record BindGroup(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() {
        wgpuBindGroupRelease(segment);
    }
}
