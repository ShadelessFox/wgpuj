package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuQuerySetRelease;

public record QuerySet(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpuQuerySetRelease(segment);
    }
}
