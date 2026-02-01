package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record ConstantEntry(String key, double value) {
    MemorySegment toNative(SegmentAllocator allocator) {
        throw new UnsupportedOperationException();
    }
}
