package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUDeviceDescriptor;
import sh.adelessfox.wgpuj.callbacks.DeviceLostCallback;
import sh.adelessfox.wgpuj.callbacks.UncapturedErrorCallback;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@WgpuStyle
@Value.Immutable
public interface DeviceDescriptor extends ObjectDescriptorBase, WgpuStruct {
    List<FeatureName> requiredFeatures();

    default Limits requiredLimits() {
        return ImmutableLimits.of();
    }

    default QueueDescriptor defaultQueue() {
        return ImmutableQueueDescriptor.of();
    }

    Optional<DeviceLostCallback> deviceLostCallback();

    Optional<UncapturedErrorCallback> uncapturedErrorCallback();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUDeviceDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label().ifPresent(x -> WgpuUtils.setString(allocator, WGPUDeviceDescriptor.label(segment), x));
        WgpuUtils.setArray(allocator, segment, requiredFeatures(), WGPUDeviceDescriptor::requiredFeatureCount, WGPUDeviceDescriptor::requiredFeatures);
        WGPUDeviceDescriptor.requiredLimits(segment, requiredLimits().toNative(allocator));
        defaultQueue().toNative(allocator, WGPUDeviceDescriptor.defaultQueue(segment));
        // TODO: callbacks
    }
}
