package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUDepthStencilState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUCompareFunction_Undefined;

@WgpuStyle
@Value.Immutable
public interface DepthStencilState extends WgpuStruct {
    TextureFormat format();

    default boolean depthWriteEnabled() {
        return false;
    }

    Optional<CompareFunction> depthCompare();

    default StencilState stencil() {
        return ImmutableStencilState.of();
    }

    default DepthBiasState bias() {
        return ImmutableDepthBiasState.of();
    }

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUDepthStencilState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUDepthStencilState.format(segment, format().value());
        WGPUDepthStencilState.depthWriteEnabled(segment, WgpuUtils.toNative(depthWriteEnabled()));
        WGPUDepthStencilState.depthCompare(segment, depthCompare().map(CompareFunction::value).orElse(WGPUCompareFunction_Undefined()));
        stencil().front().toNative(allocator, WGPUDepthStencilState.stencilFront(segment));
        stencil().back().toNative(allocator, WGPUDepthStencilState.stencilBack(segment));
        WGPUDepthStencilState.stencilReadMask(segment, stencil().readMask());
        WGPUDepthStencilState.stencilWriteMask(segment, stencil().writeMask());
        WGPUDepthStencilState.depthBias(segment, bias().constant());
        WGPUDepthStencilState.depthBiasSlopeScale(segment, bias().slopeScale());
        WGPUDepthStencilState.depthBiasClamp(segment, bias().clamp());
    }
}
