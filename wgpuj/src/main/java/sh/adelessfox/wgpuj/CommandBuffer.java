package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuCommandBufferRelease;

public record CommandBuffer(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpuCommandBufferRelease(segment);
    }
}
