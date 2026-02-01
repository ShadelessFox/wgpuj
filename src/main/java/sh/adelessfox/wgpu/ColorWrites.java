package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuFlags;

public enum ColorWrites implements WgpuFlags<ColorWrites> {
    RED(0x0000000000000001),
    GREEN(0x0000000000000002),
    BLUE(0x0000000000000004),
    ALPHA(0x0000000000000008),
    ALL(0x000000000000000F);

    private final int value;

    ColorWrites(int value) {
        this.value = value;
    }


    @Override
    public int value() {
        return value;
    }
}
