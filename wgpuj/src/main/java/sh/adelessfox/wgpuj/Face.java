package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum Face implements WgpuEnum<Face> {
    NONE(0x00000001),
    FRONT(0x00000002),
    BACK(0x00000003);

    private final int value;

    Face(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
