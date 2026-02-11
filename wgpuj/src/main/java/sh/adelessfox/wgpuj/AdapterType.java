package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum AdapterType implements WgpuEnum<AdapterType> {
    DISCRETE_GPU(0x00000001),
    INTEGRATED_GPU(0x00000002),
    CPU(0x00000003),
    UNKNOWN(0x00000004);

    private final int value;

    AdapterType(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
