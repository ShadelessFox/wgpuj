package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuFlags;

public enum BufferUsage implements WgpuFlags<BufferUsage> {
    MAP_READ(1),
    MAP_WRITE(1 << 1),
    COPY_SRC(1 << 2),
    COPY_DST(1 << 3),
    INDEX(1 << 4),
    VERTEX(1 << 5),
    UNIFORM(1 << 6),
    STORAGE(1 << 7),
    INDIRECT(1 << 8),
    QUERY_RESOLVE(1 << 9);

    private final int value;

    BufferUsage(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
