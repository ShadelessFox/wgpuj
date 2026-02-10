package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPURequestAdapterOptions;
import sh.adelessfox.wgpuj.objects.Surface;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUPowerPreference_Undefined;

@WgpuStyle
@Value.Immutable
public interface RequestAdapterOptions extends WgpuStruct {
    default FeatureLevel featureLevel() {
        return FeatureLevel.CORE;
    }

    Optional<PowerPreference> powerPreference();

    Optional<Surface> compatibleSurface();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPURequestAdapterOptions.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPURequestAdapterOptions.featureLevel(segment, featureLevel().value());
        WGPURequestAdapterOptions.powerPreference(segment, powerPreference().map(PowerPreference::value).orElse(WGPUPowerPreference_Undefined()));
    }
}
