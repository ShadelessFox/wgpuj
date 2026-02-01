package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPURequestAdapterCallback;
import sh.adelessfox.wgpu_native.WGPURequestAdapterCallbackInfo;
import sh.adelessfox.wgpu_native.WGPURequestAdapterOptions;
import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Instance(MemorySegment segment) implements WgpuObject {
    public static Instance create(InstanceDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new Instance(wgpuCreateInstance(descriptor.toNative(arena)));
        }
    }

    public Adapter requestAdapter() {
        try (Arena arena = Arena.ofConfined()) {
            var result = arena.allocate(ValueLayout.ADDRESS);

            var callback = WGPURequestAdapterCallback.allocate((status, adapter, _, userdata1, _) -> {
                if (status != WGPURequestAdapterStatus_Success()) {
                    throw new IllegalStateException("Failed to request adapter");
                }
                userdata1.reinterpret(8).set(ValueLayout.ADDRESS, 0, adapter);
            }, arena);

            var callbackInfo = WGPURequestAdapterCallbackInfo.allocate(arena);
            WGPURequestAdapterCallbackInfo.callback(callbackInfo, callback);
            WGPURequestAdapterCallbackInfo.userdata1(callbackInfo, result);

            var options = WGPURequestAdapterOptions.allocate(arena);
            wgpuInstanceRequestAdapter(arena, segment, options, callbackInfo);

            return new Adapter(result.get(ValueLayout.ADDRESS, 0));
        }
    }

    @Override
    public void close() {
        wgpuInstanceRelease(segment);
    }
}
