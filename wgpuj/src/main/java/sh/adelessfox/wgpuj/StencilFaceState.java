package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUStencilFaceState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface StencilFaceState extends WgpuStruct {
    default CompareFunction compare() {
        return CompareFunction.ALWAYS;
    }

    default StencilOperation failOp() {
        return StencilOperation.KEEP;
    }

    default StencilOperation depthFailOp() {
        return StencilOperation.KEEP;
    }

    default StencilOperation passOp() {
        return StencilOperation.KEEP;
    }

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUStencilFaceState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUStencilFaceState.compare(segment, compare().value());
        WGPUStencilFaceState.failOp(segment, failOp().value());
        WGPUStencilFaceState.depthFailOp(segment, depthFailOp().value());
        WGPUStencilFaceState.passOp(segment, passOp().value());
    }
}
