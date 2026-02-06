package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBufferBindingLayout;
import sh.adelessfox.wgpu_native.WGPUSamplerBindingLayout;
import sh.adelessfox.wgpu_native.WGPUStorageTextureBindingLayout;
import sh.adelessfox.wgpu_native.WGPUTextureBindingLayout;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemorySegment;
import java.util.OptionalLong;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

@Value.Enclosing
public sealed interface BindingType {
    @Value.Builder
    record Buffer(
        BufferBindingType type,
        boolean hasDynamicOffset,
        OptionalLong minBindingSize
    ) implements BindingType {
        public static ImmutableBindingType.BufferBuilder builder() {
            return ImmutableBindingType.bufferBuilder();
        }
    }

    @Value.Builder
    record Sampler(
        SamplerBindingType type
    ) implements BindingType {
        public static ImmutableBindingType.SamplerBuilder builder() {
            return ImmutableBindingType.samplerBuilder();
        }
    }

    @Value.Builder
    record Texture(
        TextureSampleType sampleType,
        TextureViewDimension viewDimension,
        boolean multisampled
    ) implements BindingType {
        public static ImmutableBindingType.TextureBuilder builder() {
            return ImmutableBindingType.textureBuilder();
        }
    }

    @Value.Builder
    record StorageTexture(
        StorageTextureAccess access,
        TextureFormat format,
        TextureViewDimension viewDimension
    ) implements BindingType {
        public static ImmutableBindingType.StorageTextureBuilder builder() {
            return ImmutableBindingType.storageTextureBuilder();
        }
    }

    default void toNativeBuffer(MemorySegment segment) {
        if (this instanceof Buffer(var type, var hasDynamicOffset, var minBindingSize)) {
            WGPUBufferBindingLayout.type(segment, type.toNative());
            WGPUBufferBindingLayout.hasDynamicOffset(segment, WgpuUtils.toNative(hasDynamicOffset));
            WGPUBufferBindingLayout.minBindingSize(segment, minBindingSize.orElse(0));
        } else {
            WGPUBufferBindingLayout.type(segment, WGPUBufferBindingType_BindingNotUsed());
        }
    }

    default void toNativeSampler(MemorySegment segment) {
        if (this instanceof Sampler(var type)) {
            WGPUSamplerBindingLayout.type(segment, type.value());
        } else {
            WGPUSamplerBindingLayout.type(segment, WGPUSamplerBindingType_BindingNotUsed());
        }
    }

    default void toNativeTexture(MemorySegment segment) {
        if (this instanceof Texture(var sampleType, var viewDimension, var multisampled)) {
            WGPUTextureBindingLayout.sampleType(segment, sampleType.toNative());
            WGPUTextureBindingLayout.viewDimension(segment, viewDimension.value());
            WGPUTextureBindingLayout.multisampled(segment, WgpuUtils.toNative(multisampled));
        } else {
            WGPUTextureBindingLayout.sampleType(segment, WGPUTextureSampleType_BindingNotUsed());
        }
    }

    default void toNativeStorageTexture(MemorySegment segment) {
        if (this instanceof StorageTexture(var access, var format, var viewDimension)) {
            WGPUStorageTextureBindingLayout.access(segment, access.value());
            WGPUStorageTextureBindingLayout.format(segment, format.value());
            WGPUStorageTextureBindingLayout.viewDimension(segment, viewDimension.value());
        } else {
            WGPUStorageTextureBindingLayout.access(segment, WGPUStorageTextureAccess_BindingNotUsed());
        }
    }
}
