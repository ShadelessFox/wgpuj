package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpu_native.WGPURequestAdapterCallback;
import sh.adelessfox.wgpu_native.WGPURequestAdapterCallbackInfo;
import sh.adelessfox.wgpuj.InstanceDescriptor;
import sh.adelessfox.wgpuj.RequestAdapterOptions;
import sh.adelessfox.wgpuj.SurfaceDescriptor;
import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Instance(MemorySegment segment) implements WgpuObject {
    public static Instance create(InstanceDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new Instance(wgpuCreateInstance(descriptor.toNative(arena)));
        }
    }

    public Surface createSurface(SurfaceDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new Surface(wgpuInstanceCreateSurface(segment, descriptor.toNative(arena)));
        }
    }

    public Adapter requestAdapter() {
        return requestAdapter(Optional.empty());
    }

    public Adapter requestAdapter(RequestAdapterOptions options) {
        return requestAdapter(Optional.of(options));
    }

    private Adapter requestAdapter(Optional<RequestAdapterOptions> options) {
        try (Arena arena = Arena.ofConfined()) {
            var result = new MemorySegment[1];

            var callback = WGPURequestAdapterCallbackInfo.allocate(arena);
            WGPURequestAdapterCallbackInfo.callback(callback, WGPURequestAdapterCallback.allocate((status, adapter, _, _, _) -> {
                if (status != WGPURequestAdapterStatus_Success()) {
                    throw new IllegalStateException("Failed to request adapter");
                }
                result[0] = adapter;
            }, arena));

            wgpuInstanceRequestAdapter(
                arena,
                segment,
                options.map(x -> x.toNative(arena)).orElse(MemorySegment.NULL),
                callback);

            return new Adapter(result[0]);
        }
    }

    @Override
    public void close() {
        wgpuInstanceRelease(segment);
    }
}
