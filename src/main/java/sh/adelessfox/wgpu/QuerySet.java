package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuQuerySetRelease;

public record QuerySet(MemorySegment segment) implements AutoCloseable {
    @Override
    public void close() {
        wgpuQuerySetRelease(segment);
    }
}
