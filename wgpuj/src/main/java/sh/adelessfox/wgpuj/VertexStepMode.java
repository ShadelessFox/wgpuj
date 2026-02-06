package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum VertexStepMode implements WgpuEnum<VertexStepMode> {
    VERTEX_BUFFER_NOT_USED(0x00000000),
    VERTEX(0x00000002),
    INSTANCE(0x00000003);

    private final int value;

    VertexStepMode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
