package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuEnum;

public enum TextureViewDimension implements WgpuEnum<TextureViewDimension> {
    UNDEFINED(0x00000000),
    D1(0x00000001),
    D2(0x00000002),
    D2_ARRAY(0x00000003),
    CUBE(0x00000004),
    CUBE_ARRAY(0x00000005),
    D3(0x00000006);

    private final int value;

    TextureViewDimension(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
