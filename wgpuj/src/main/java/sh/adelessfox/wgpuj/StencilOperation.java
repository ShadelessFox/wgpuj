package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum StencilOperation implements WgpuEnum<StencilOperation> {
    KEEP(0x00000001),
    ZERO(0x00000002),
    REPLACE(0x00000003),
    INVERT(0x00000004),
    INCREMENT_CLAMP(0x00000005),
    DECREMENT_CLAMP(0x00000006),
    INCREMENT_WRAP(0x00000007),
    DECREMENT_WRAP(0x00000008);

    private final int value;

    StencilOperation(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
