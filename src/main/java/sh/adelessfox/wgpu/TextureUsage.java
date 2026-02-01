package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuFlags;

public enum TextureUsage implements WgpuFlags<TextureUsage> {
    COPY_SRC(1),
    COPY_DST(1 << 1),
    TEXTURE_BINDING(1 << 2),
    STORAGE_BINDING(1 << 3),
    RENDER_ATTACHMENT(1 << 4);

    private final int value;

    TextureUsage(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
