package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum PrimitiveTopology implements WgpuEnum<PrimitiveTopology> {
    POINT_LIST(0x00000001),
    LINE_LIST(0x00000002),
    LINE_STRIP(0x00000003),
    TRIANGLE_LIST(0x00000004),
    TRIANGLE_STRIP(0x00000005);

    private final int value;

    PrimitiveTopology(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
