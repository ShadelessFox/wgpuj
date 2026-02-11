package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpu_native.WGPUSurfaceCapabilities;
import sh.adelessfox.wgpu_native.WGPUSurfaceTexture;
import sh.adelessfox.wgpuj.SurfaceCapabilities;
import sh.adelessfox.wgpuj.SurfaceConfiguration;
import sh.adelessfox.wgpuj.SurfaceTexture;
import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Surface(MemorySegment segment) implements WgpuObject {
    public void present() {
        wgpuSurfacePresent(segment);
    }

    public void configure(SurfaceConfiguration configuration) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuSurfaceConfigure(segment, configuration.toNative(arena));
        }
    }

    public void unconfigure() {
        wgpuSurfaceUnconfigure(segment);
    }

    public SurfaceCapabilities getCapabilities(Adapter adapter) {
        try (Arena arena = Arena.ofConfined()) {
            var segment = WGPUSurfaceCapabilities.allocate(arena);
            wgpuSurfaceGetCapabilities(this.segment, adapter.segment(), segment);
            var result = SurfaceCapabilities.ofNative(segment);
            wgpuSurfaceCapabilitiesFreeMembers(segment);
            return result;
        }
    }

    public SurfaceTexture getCurrentTexture() {
        try (Arena arena = Arena.ofConfined()) {
            var segment = WGPUSurfaceTexture.allocate(arena);
            wgpuSurfaceGetCurrentTexture(this.segment, segment);
            return SurfaceTexture.ofNative(segment);
        }
    }

    @Override
    public void close() {
        wgpuSurfaceRelease(segment);
    }
}
