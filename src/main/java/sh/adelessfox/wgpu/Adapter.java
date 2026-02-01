package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuObject;
import sh.adelessfox.wgpu_native.WGPURequestDeviceCallback;
import sh.adelessfox.wgpu_native.WGPURequestDeviceCallbackInfo;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Adapter(MemorySegment segment) implements WgpuObject {
    public Device requestDevice(DeviceDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            var result = arena.allocate(ValueLayout.ADDRESS);

            var callback = WGPURequestDeviceCallback.allocate((status, device, _, userdata1, _) -> {
                if (status != WGPURequestDeviceStatus_Success()) {
                    throw new IllegalStateException("Failed to request device");
                }
                userdata1.reinterpret(8).set(ValueLayout.ADDRESS, 0, device);
            }, arena);

            var callbackInfo = WGPURequestDeviceCallbackInfo.allocate(arena);
            WGPURequestDeviceCallbackInfo.callback(callbackInfo, callback);
            WGPURequestDeviceCallbackInfo.userdata1(callbackInfo, result);

            wgpuAdapterRequestDevice(arena, segment, descriptor.toNative(arena), callbackInfo);

            return new Device(result.get(ValueLayout.ADDRESS, 0));
        }
    }

    @Override
    public void close() {
        wgpuAdapterRelease(segment);
    }
}
