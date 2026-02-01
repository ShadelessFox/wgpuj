package sh.adelessfox.wgpu.util;

import java.lang.foreign.MemorySegment;

public interface WgpuObject extends AutoCloseable {
    MemorySegment segment();

    @Override
    void close();
}
