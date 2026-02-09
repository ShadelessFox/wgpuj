package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum QueryType implements WgpuEnum<QueryType> {
    OCCLUSION(0x00000001),
    TIMESTAMP(0x00000002);

    private final int value;

    QueryType(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
