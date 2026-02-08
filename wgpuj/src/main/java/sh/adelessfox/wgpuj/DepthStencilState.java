package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUDepthStencilState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

@Value.Builder
public record DepthStencilState(
    TextureFormat format,
    boolean depthWriteEnabled,
    CompareFunction depthCompare,
    StencilState stencil,
    Optional<DepthBiasState> bias
) implements WgpuStruct {
    public static DepthStencilStateBuilder builder() {
        return new DepthStencilStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUDepthStencilState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUDepthStencilState.format(segment, format.value());
        WGPUDepthStencilState.depthWriteEnabled(segment, WgpuUtils.toNative(depthWriteEnabled));
        WGPUDepthStencilState.depthCompare(segment, depthCompare.value());
        stencil.front().toNative(allocator, WGPUDepthStencilState.stencilFront(segment));
        stencil.back().toNative(allocator, WGPUDepthStencilState.stencilBack(segment));
        WGPUDepthStencilState.stencilReadMask(segment, stencil.readMask().orElse(-1));
        WGPUDepthStencilState.stencilWriteMask(segment, stencil.writeMask().orElse(-1));
        bias.ifPresent(bias -> {
            WGPUDepthStencilState.depthBias(segment, bias.constant());
            WGPUDepthStencilState.depthBiasSlopeScale(segment, bias.slopeScale());
            WGPUDepthStencilState.depthBiasClamp(segment, bias.clamp());
        });
    }
}
