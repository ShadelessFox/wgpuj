package sh.adelessfox.wgpu;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record RenderPassDepthStencilAttachment() {
    MemorySegment toNative(SegmentAllocator allocator) {
        throw new UnsupportedOperationException();
    }
}
