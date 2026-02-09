package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPULimits;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_LIMIT_U32_UNDEFINED;
import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_LIMIT_U64_UNDEFINED;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface Limits extends WgpuStruct {
    OptionalInt maxTextureDimension1D();

    OptionalInt maxTextureDimension2D();

    OptionalInt maxTextureDimension3D();

    OptionalInt maxTextureArrayLayers();

    OptionalInt maxBindGroups();

    OptionalInt maxBindGroupsPlusVertexBuffers();

    OptionalInt maxBindingsPerBindGroup();

    OptionalInt maxDynamicUniformBuffersPerPipelineLayout();

    OptionalInt maxDynamicStorageBuffersPerPipelineLayout();

    OptionalInt maxSampledTexturesPerShaderStage();

    OptionalInt maxSamplersPerShaderStage();

    OptionalInt maxStorageBuffersPerShaderStage();

    OptionalInt maxStorageTexturesPerShaderStage();

    OptionalInt maxUniformBuffersPerShaderStage();

    OptionalLong maxUniformBufferBindingSize();

    OptionalLong maxStorageBufferBindingSize();

    OptionalInt minUniformBufferOffsetAlignment();

    OptionalInt minStorageBufferOffsetAlignment();

    OptionalInt maxVertexBuffers();

    OptionalLong maxBufferSize();

    OptionalInt maxVertexAttributes();

    OptionalInt maxVertexBufferArrayStride();

    OptionalInt maxInterStageShaderVariables();

    OptionalInt maxColorAttachments();

    OptionalInt maxColorAttachmentBytesPerSample();

    OptionalInt maxComputeWorkgroupStorageSize();

    OptionalInt maxComputeInvocationsPerWorkgroup();

    OptionalInt maxComputeWorkgroupSizeX();

    OptionalInt maxComputeWorkgroupSizeY();

    OptionalInt maxComputeWorkgroupSizeZ();

    OptionalInt maxComputeWorkgroupsPerDimension();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPULimits.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPULimits.maxTextureDimension1D(segment, maxTextureDimension1D().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxTextureDimension2D(segment, maxTextureDimension2D().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxTextureDimension3D(segment, maxTextureDimension3D().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxTextureArrayLayers(segment, maxTextureArrayLayers().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxBindGroups(segment, maxBindGroups().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxBindGroupsPlusVertexBuffers(segment, maxBindGroupsPlusVertexBuffers().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxBindingsPerBindGroup(segment, maxBindingsPerBindGroup().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxDynamicUniformBuffersPerPipelineLayout(segment, maxDynamicUniformBuffersPerPipelineLayout().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxDynamicStorageBuffersPerPipelineLayout(segment, maxDynamicStorageBuffersPerPipelineLayout().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxSampledTexturesPerShaderStage(segment, maxSampledTexturesPerShaderStage().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxSamplersPerShaderStage(segment, maxSamplersPerShaderStage().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxStorageBuffersPerShaderStage(segment, maxStorageBuffersPerShaderStage().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxStorageTexturesPerShaderStage(segment, maxStorageTexturesPerShaderStage().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxUniformBuffersPerShaderStage(segment, maxUniformBuffersPerShaderStage().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxUniformBufferBindingSize(segment, maxUniformBufferBindingSize().orElse(WGPU_LIMIT_U64_UNDEFINED()));
        WGPULimits.maxStorageBufferBindingSize(segment, maxStorageBufferBindingSize().orElse(WGPU_LIMIT_U64_UNDEFINED()));
        WGPULimits.minUniformBufferOffsetAlignment(segment, minUniformBufferOffsetAlignment().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.minStorageBufferOffsetAlignment(segment, minStorageBufferOffsetAlignment().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxVertexBuffers(segment, maxVertexBuffers().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxBufferSize(segment, maxBufferSize().orElse(WGPU_LIMIT_U64_UNDEFINED()));
        WGPULimits.maxVertexAttributes(segment, maxVertexAttributes().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxVertexBufferArrayStride(segment, maxVertexBufferArrayStride().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxInterStageShaderVariables(segment, maxInterStageShaderVariables().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxColorAttachments(segment, maxColorAttachments().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxColorAttachmentBytesPerSample(segment, maxColorAttachmentBytesPerSample().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxComputeWorkgroupStorageSize(segment, maxComputeWorkgroupStorageSize().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxComputeInvocationsPerWorkgroup(segment, maxComputeInvocationsPerWorkgroup().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxComputeWorkgroupSizeX(segment, maxComputeWorkgroupSizeX().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxComputeWorkgroupSizeY(segment, maxComputeWorkgroupSizeY().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxComputeWorkgroupSizeZ(segment, maxComputeWorkgroupSizeZ().orElse(WGPU_LIMIT_U32_UNDEFINED()));
        WGPULimits.maxComputeWorkgroupsPerDimension(segment, maxComputeWorkgroupsPerDimension().orElse(WGPU_LIMIT_U32_UNDEFINED()));
    }
}
