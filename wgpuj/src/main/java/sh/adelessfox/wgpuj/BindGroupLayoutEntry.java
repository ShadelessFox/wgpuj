package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupLayoutEntry;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Set;

@WgpuStyle
@Value.Immutable
public interface BindGroupLayoutEntry extends WgpuStruct {
    int binding();

    Set<ShaderStage> visibility();

    BindingType type();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUBindGroupLayoutEntry.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUBindGroupLayoutEntry.binding(segment, binding());
        WGPUBindGroupLayoutEntry.visibility(segment, WgpuFlags.toNative(visibility()));
        type().toNativeBuffer(WGPUBindGroupLayoutEntry.buffer(segment));
        type().toNativeSampler(WGPUBindGroupLayoutEntry.sampler(segment));
        type().toNativeTexture(WGPUBindGroupLayoutEntry.texture(segment));
        type().toNativeStorageTexture(WGPUBindGroupLayoutEntry.storageTexture(segment));
    }
}
