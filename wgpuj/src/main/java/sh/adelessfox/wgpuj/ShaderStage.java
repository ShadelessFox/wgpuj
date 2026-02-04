package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuFlags;

public enum ShaderStage implements WgpuFlags<ShaderStage> {
    VERTEX(0x0000000000000001),
    FRAGMENT(0x0000000000000002),
    COMPUTE(0x0000000000000004);

    private final int value;

    ShaderStage(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
