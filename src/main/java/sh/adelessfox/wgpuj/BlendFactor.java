package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum BlendFactor implements WgpuEnum<BlendFactor> {
    UNDEFINED(0x00000000),
    ZERO(0x00000001),
    ONE(0x00000002),
    SRC(0x00000003),
    ONE_MINUS_SRC(0x00000004),
    SRC_ALPHA(0x00000005),
    ONE_MINUS_SRC_ALPHA(0x00000006),
    DST(0x00000007),
    ONE_MINUS_DST(0x00000008),
    DST_ALPHA(0x00000009),
    ONE_MINUS_DST_ALPHA(0x0000000A),
    SRC_ALPHA_SATURATED(0x0000000B),
    CONSTANT(0x0000000C),
    ONE_MINUS_CONSTANT(0x0000000D),
    SRC1(0x0000000E),
    ONE_MINUS_SRC1(0x0000000F),
    SRC1ALPHA(0x00000010),
    ONE_MINUS_SRC1ALPHA(0x00000011);

    private final int value;

    BlendFactor(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
