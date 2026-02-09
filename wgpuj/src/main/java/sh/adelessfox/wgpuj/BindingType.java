package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBufferBindingLayout;
import sh.adelessfox.wgpu_native.WGPUSamplerBindingLayout;
import sh.adelessfox.wgpu_native.WGPUStorageTextureBindingLayout;
import sh.adelessfox.wgpu_native.WGPUTextureBindingLayout;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

@WgpuStyle
@Value.Enclosing
public sealed interface BindingType {
    @WgpuStyle
    @Value.Immutable(singleton = true)
    non-sealed interface Buffer extends BindingType {
        default BufferBindingType type() {
            return new BufferBindingType.Uniform();
        }

        default boolean hasDynamicOffset() {
            return false;
        }

        default long minBindingSize() {
            return 0;
        }
    }

    @WgpuStyle
    @Value.Immutable(singleton = true)
    non-sealed interface Sampler extends BindingType {
        default SamplerBindingType type() {
            return SamplerBindingType.FILTERING;
        }
    }

    @WgpuStyle
    @Value.Immutable(singleton = true)
    non-sealed interface Texture extends BindingType {
        default TextureSampleType sampleType() {
            return new TextureSampleType.Float(true);
        }

        default TextureViewDimension viewDimension() {
            return TextureViewDimension.D2;
        }

        default boolean multisampled() {
            return false;
        }
    }

    @WgpuStyle
    @Value.Immutable
    non-sealed interface StorageTexture extends BindingType {
        default StorageTextureAccess access() {
            return StorageTextureAccess.WRITE_ONLY;
        }

        TextureFormat format();

        default TextureViewDimension viewDimension() {
            return TextureViewDimension.D2;
        }
    }

    default void toNativeBuffer(MemorySegment segment) {
        if (this instanceof Buffer buffer) {
            WGPUBufferBindingLayout.type(segment, buffer.type().toNative());
            WGPUBufferBindingLayout.hasDynamicOffset(segment, WgpuUtils.toNative(buffer.hasDynamicOffset()));
            WGPUBufferBindingLayout.minBindingSize(segment, buffer.minBindingSize());
        } else {
            WGPUBufferBindingLayout.type(segment, WGPUBufferBindingType_BindingNotUsed());
        }
    }

    default void toNativeSampler(MemorySegment segment) {
        if (this instanceof Sampler sampler) {
            WGPUSamplerBindingLayout.type(segment, sampler.type().value());
        } else {
            WGPUSamplerBindingLayout.type(segment, WGPUSamplerBindingType_BindingNotUsed());
        }
    }

    default void toNativeTexture(MemorySegment segment) {
        if (this instanceof Texture texture) {
            WGPUTextureBindingLayout.sampleType(segment, texture.sampleType().toNative());
            WGPUTextureBindingLayout.viewDimension(segment, texture.viewDimension().value());
            WGPUTextureBindingLayout.multisampled(segment, WgpuUtils.toNative(texture.multisampled()));
        } else {
            WGPUTextureBindingLayout.sampleType(segment, WGPUTextureSampleType_BindingNotUsed());
        }
    }

    default void toNativeStorageTexture(MemorySegment segment) {
        if (this instanceof StorageTexture texture) {
            WGPUStorageTextureBindingLayout.access(segment, texture.access().value());
            WGPUStorageTextureBindingLayout.format(segment, texture.format().value());
            WGPUStorageTextureBindingLayout.viewDimension(segment, texture.viewDimension().value());
        } else {
            WGPUStorageTextureBindingLayout.access(segment, WGPUStorageTextureAccess_BindingNotUsed());
        }
    }
}
