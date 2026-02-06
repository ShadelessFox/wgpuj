package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum TextureDimension implements WgpuEnum<TextureDimension> {
    D1(0x00000001),
    D2(0x00000002),
    D3(0x00000003);

    private final int value;

    TextureDimension(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
