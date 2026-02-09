package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUPrimitiveState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUIndexFormat_Undefined;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface PrimitiveState extends WgpuStruct {
    default PrimitiveTopology topology() {
        return PrimitiveTopology.TRIANGLE_LIST;
    }

    Optional<IndexFormat> stripIndexFormat();

    default FrontFace frontFace() {
        return FrontFace.CCW;
    }

    default Face cullMode() {
        return Face.NONE;
    }

    default boolean unclippedDepth() {
        return false;
    }

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUPrimitiveState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUPrimitiveState.topology(segment, topology().value());
        WGPUPrimitiveState.stripIndexFormat(segment, stripIndexFormat().map(IndexFormat::value).orElse(WGPUIndexFormat_Undefined()));
        WGPUPrimitiveState.frontFace(segment, frontFace().value());
        WGPUPrimitiveState.cullMode(segment, cullMode().value());
        WGPUPrimitiveState.unclippedDepth(segment, WgpuUtils.toNative(unclippedDepth()));
    }
}
