package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum StorageTextureAccess implements WgpuEnum<StorageTextureAccess> {
    WRITE_ONLY(0x00000002),
    READ_ONLY(0x00000003),
    READ_WRITE(0x00000004);

    private final int value;

    StorageTextureAccess(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
