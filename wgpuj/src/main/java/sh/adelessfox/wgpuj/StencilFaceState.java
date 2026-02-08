package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUStencilFaceState;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record StencilFaceState(
    CompareFunction compare,
    StencilOperation failOp,
    StencilOperation depthFailOp,
    StencilOperation passOp
) implements WgpuStruct {
    public static final StencilFaceState IGNORE = new StencilFaceState(
        CompareFunction.ALWAYS,
        StencilOperation.KEEP,
        StencilOperation.KEEP,
        StencilOperation.KEEP
    );

    public static StencilFaceStateBuilder builder() {
        return new StencilFaceStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUStencilFaceState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUStencilFaceState.compare(segment, compare.value());
        WGPUStencilFaceState.failOp(segment, failOp.value());
        WGPUStencilFaceState.depthFailOp(segment, depthFailOp.value());
        WGPUStencilFaceState.passOp(segment, passOp.value());
    }
}
