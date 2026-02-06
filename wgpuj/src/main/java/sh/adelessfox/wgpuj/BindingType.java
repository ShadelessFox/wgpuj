package sh.adelessfox.wgpuj;

import org.immutables.value.Value;

import java.util.OptionalLong;

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
}
