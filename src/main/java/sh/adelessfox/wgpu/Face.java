package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuEnum;

public enum Face implements WgpuEnum<Face> {
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
