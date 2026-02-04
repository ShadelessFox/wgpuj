package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPURenderPassTimestampWrites;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record RenderPassTimestampWrites(
    QuerySet querySet,
    int beginningOfPassWriteIndex,
    int endOfPassWriteIndex
) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPURenderPassTimestampWrites.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPURenderPassTimestampWrites.querySet(segment, querySet.segment());
        WGPURenderPassTimestampWrites.beginningOfPassWriteIndex(segment, beginningOfPassWriteIndex);
        WGPURenderPassTimestampWrites.endOfPassWriteIndex(segment, endOfPassWriteIndex);
    }
}
