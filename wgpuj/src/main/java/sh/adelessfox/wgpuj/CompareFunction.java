package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum CompareFunction implements WgpuEnum<CompareFunction> {
    NEVER(0x00000001),
    LESS(0x00000002),
    EQUAL(0x00000003),
    LESS_EQUAL(0x00000004),
    GREATER(0x00000005),
    NOT_EQUAL(0x00000006),
    GREATER_EQUAL(0x00000007),
    ALWAYS(0x00000008);

    private final int value;

    CompareFunction(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
