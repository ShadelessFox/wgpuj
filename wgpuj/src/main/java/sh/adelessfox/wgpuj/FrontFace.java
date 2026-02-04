package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum FrontFace implements WgpuEnum<FrontFace> {
    CCW(0x00000001),
    CW(0x00000002);

    private final int value;

    FrontFace(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
