package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum BlendOperation implements WgpuEnum<BlendOperation> {
    ADD(0x00000001),
    SUBTRACT(0x00000002),
    REVERSE_SUBTRACT(0x00000003),
    MIN(0x00000004),
    MAX(0x00000005);

    private final int value;

    BlendOperation(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
