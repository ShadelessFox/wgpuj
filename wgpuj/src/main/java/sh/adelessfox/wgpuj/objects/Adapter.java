package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpu_native.WGPUAdapterInfo;
import sh.adelessfox.wgpu_native.WGPURequestDeviceCallback;
import sh.adelessfox.wgpu_native.WGPURequestDeviceCallbackInfo;
import sh.adelessfox.wgpuj.AdapterInfo;
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

            return new Device(MemorySegment.ofAddress(result[0].address()));
        }
    }

    public AdapterInfo getInfo() {
        try (Arena arena = Arena.ofConfined()) {
            var segment = WGPUAdapterInfo.allocate(arena);
            wgpuAdapterGetInfo(this.segment, segment);
            var result = AdapterInfo.ofNative(segment);
            wgpuAdapterInfoFreeMembers(segment);
            return result;
        }
    }

    @Override
    public void close() {
        wgpuAdapterRelease(segment);
    }
}
