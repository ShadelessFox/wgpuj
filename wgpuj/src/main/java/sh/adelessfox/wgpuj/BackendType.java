package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum BackendType implements WgpuEnum<BackendType> {
    NULL(0x00000001),
    WEBGPU(0x00000002),
    D3D11(0x00000003),
    D3D12(0x00000004),
    METAL(0x00000005),
    VULKAN(0x00000006),
    OPENGL(0x00000007),
    OPENGL_ES(0x00000008);

    private final int value;

    BackendType(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
