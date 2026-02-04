package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuFlags;

public enum InstanceFlag implements WgpuFlags<InstanceFlag> {
    DEBUG(1),
    VALIDATION(1 << 1);

    private final int value;

    InstanceFlag(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
