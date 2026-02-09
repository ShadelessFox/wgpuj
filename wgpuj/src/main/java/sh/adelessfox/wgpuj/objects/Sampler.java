package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpuj.util.WgpuObject;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.wgpuSamplerRelease;
import static sh.adelessfox.wgpu_native.wgpu_h.wgpuSamplerSetLabel;

public record Sampler(MemorySegment segment) implements WgpuObject {
    public void setLabel(String label) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuSamplerSetLabel(segment, WgpuUtils.toNative(arena, label));
        }
    }

    @Override
    public void close() {
        wgpuSamplerRelease(segment);
    }
}
