package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum TextureAspect implements WgpuEnum<TextureAspect> {
    ALL(0x00000001),
    STENCIL_ONLY(0x00000002),
    DEPTH_ONLY(0x00000003);

    private final int value;

    TextureAspect(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
