package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum SamplerBindingType implements WgpuEnum<SamplerBindingType> {
    FILTERING(0x00000002),
    NON_FILTERING(0x00000003),
    COMPARISON(0x00000004);

    private final int value;

    SamplerBindingType(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
