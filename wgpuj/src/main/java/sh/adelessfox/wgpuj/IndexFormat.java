package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum IndexFormat implements WgpuEnum<IndexFormat> {
    UINT16(0x00000001),
    UINT32(0x00000002);

    private final int value;

    IndexFormat(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
