package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuBindGroupLayoutRelease;

public record BindGroupLayout(MemorySegment segment) implements WgpuObject {
    @Override
    public void close() {
        wgpuBindGroupLayoutRelease(segment);
    }
}
