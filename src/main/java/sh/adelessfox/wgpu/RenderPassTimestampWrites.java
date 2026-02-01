package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPURenderPassTimestampWrites;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record RenderPassTimestampWrites(
    QuerySet querySet,
    int beginningOfPassWriteIndex,
    int endOfPassWriteIndex
) implements WgpuStruct {
    @Override
    public MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPURenderPassTimestampWrites.allocate(allocator);
        WGPURenderPassTimestampWrites.querySet(segment, querySet.segment());
        WGPURenderPassTimestampWrites.beginningOfPassWriteIndex(segment, beginningOfPassWriteIndex);
        WGPURenderPassTimestampWrites.endOfPassWriteIndex(segment, endOfPassWriteIndex);
        return segment;
    }
}
