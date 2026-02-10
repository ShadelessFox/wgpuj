package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuSurfaceRelease;

public record Surface(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpuSurfaceRelease(segment);
    }
}
