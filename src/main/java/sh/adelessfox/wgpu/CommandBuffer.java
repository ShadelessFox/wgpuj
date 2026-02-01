package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu_native.wgpu_h;

import java.lang.foreign.MemorySegment;

public record CommandBuffer(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() {
        wgpu_h.wgpuCommandBufferRelease(segment);
    }
}
