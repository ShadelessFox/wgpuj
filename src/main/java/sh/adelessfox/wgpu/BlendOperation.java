package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuEnum;

public enum BlendOperation implements WgpuEnum<BlendOperation> {
    UNDEFINED(0x00000000),
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
