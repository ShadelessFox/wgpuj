package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUAdapterInfo;
import sh.adelessfox.wgpuj.util.WgpuEnum;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface AdapterInfo extends WgpuStruct {
    static AdapterInfo ofNative(MemorySegment segment) {
        var vendor = WgpuUtils.getString(WGPUAdapterInfo.vendor(segment));
        var architecture = WgpuUtils.getString(WGPUAdapterInfo.architecture(segment));
        var device = WgpuUtils.getString(WGPUAdapterInfo.device(segment));
        var description = WgpuUtils.getString(WGPUAdapterInfo.description(segment));
        var backendType = WgpuEnum.ofNative(WGPUAdapterInfo.backendType(segment), BackendType.class);
        var adapterType = WgpuEnum.ofNative(WGPUAdapterInfo.adapterType(segment), AdapterType.class);
        var vendorId = WGPUAdapterInfo.vendorID(segment);
        var deviceId = WGPUAdapterInfo.deviceID(segment);
        return ImmutableAdapterInfo.builder()
            .vendor(vendor)
            .architecture(architecture)
            .device(device)
            .description(description)
            .backendType(backendType)
            .adapterType(adapterType)
            .vendorId(vendorId)
            .deviceId(deviceId)
            .build();
    }

    String vendor();

    String architecture();

    String device();

    String description();

    BackendType backendType();

    AdapterType adapterType();

    int vendorId();

    int deviceId();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUAdapterInfo.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        throw new UnsupportedOperationException();
    }
}
