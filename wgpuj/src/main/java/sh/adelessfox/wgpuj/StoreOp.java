package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum StoreOp implements WgpuEnum<StoreOp> {
    UNDEFINED(0x00000000),
    STORE(0x00000001),
    DISCARD(0x00000002);

    private final int value;

    StoreOp(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
