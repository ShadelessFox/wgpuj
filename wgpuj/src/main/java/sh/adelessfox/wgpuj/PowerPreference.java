package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum PowerPreference implements WgpuEnum<PowerPreference> {
    LOW_POWER(0x00000001),
    HIGH_PERFORMANCE(0x00000002);

    private final int value;

    PowerPreference(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
