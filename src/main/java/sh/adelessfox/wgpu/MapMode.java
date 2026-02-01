package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuFlags;

public enum MapMode implements WgpuFlags<MapMode> {
    READ(0x0000000000000001),
    WRITE(0x0000000000000002);

    private final int value;

    MapMode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
