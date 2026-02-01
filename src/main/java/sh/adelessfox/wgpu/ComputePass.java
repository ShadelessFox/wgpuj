package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuObject;
import sh.adelessfox.wgpu_native.wgpu_h;

import java.lang.foreign.MemorySegment;

public record ComputePass(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpu_h.wgpuComputePassEncoderRelease(segment);
    }
}
