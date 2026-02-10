package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpu_native.WGPURequestDeviceCallback;
import sh.adelessfox.wgpu_native.WGPURequestDeviceCallbackInfo;
import sh.adelessfox.wgpuj.DeviceDescriptor;
import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Adapter(MemorySegment segment) implements WgpuObject {
    public Device requestDevice() {
        return requestDevice(Optional.empty());
    }

    public Device requestDevice(DeviceDescriptor descriptor) {
        return requestDevice(Optional.of(descriptor));
    }

    private Device requestDevice(Optional<DeviceDescriptor> descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            var result = new MemorySegment[1];
            var callback = WGPURequestDeviceCallbackInfo.allocate(arena);
            WGPURequestDeviceCallbackInfo.callback(callback, WGPURequestDeviceCallback.allocate((status, device, _, _, _) -> {
                if (status != WGPURequestDeviceStatus_Success()) {
                    throw new IllegalStateException("Failed to request device");
                }
                result[0] = device;
            }, arena));

            wgpuAdapterRequestDevice(
                arena,
                segment,
                descriptor.map(x -> x.toNative(arena)).orElse(MemorySegment.NULL),
                callback);

            return new Device(result[0]);
        }
    }

    @Override
    public void close() {
        wgpuAdapterRelease(segment);
    }
}
