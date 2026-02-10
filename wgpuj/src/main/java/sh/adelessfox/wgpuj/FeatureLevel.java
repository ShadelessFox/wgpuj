package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum FeatureLevel implements WgpuEnum<FeatureLevel> {
    /** "Compatibility" profile which can be supported on OpenGL ES 3.1. */
    COMPATIBILITY(0x00000001),
    /** "Core" profile which can be supported on Vulkan/Metal/D3D12. */
    CORE(0x00000002);

    private final int value;

    FeatureLevel(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
