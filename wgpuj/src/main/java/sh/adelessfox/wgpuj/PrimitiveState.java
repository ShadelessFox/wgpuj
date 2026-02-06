package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUPrimitiveState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUCullMode_None;
import static sh.adelessfox.wgpu_native.wgpu_h.WGPUIndexFormat_Undefined;

@Value.Builder
public record PrimitiveState(
    PrimitiveTopology topology,
    Optional<IndexFormat> stripIndexFormat,
    FrontFace frontFace,
    Optional<Face> cullMode,
    @Value.Default.Boolean(false)
    boolean unclippedDepth
) implements WgpuStruct {
    public static PrimitiveStateBuilder builder() {
        return new PrimitiveStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUPrimitiveState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUPrimitiveState.topology(segment, topology.value());
        WGPUPrimitiveState.stripIndexFormat(segment, stripIndexFormat.map(IndexFormat::value).orElse(WGPUIndexFormat_Undefined()));
        WGPUPrimitiveState.frontFace(segment, frontFace.value());
        WGPUPrimitiveState.cullMode(segment, cullMode.map(Face::value).orElse(WGPUCullMode_None()));
        WGPUPrimitiveState.unclippedDepth(segment, WgpuUtils.toNative(unclippedDepth));
    }
}
