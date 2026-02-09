package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUComputePassTimestampWrites;
import sh.adelessfox.wgpuj.objects.QuerySet;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.OptionalInt;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPU_QUERY_SET_INDEX_UNDEFINED;

@WgpuStyle
@Value.Immutable
public interface ComputePassTimestampWrites extends WgpuStruct {
    QuerySet querySet();

    OptionalInt beginningOfPassWriteIndex();

    OptionalInt endOfPassWriteIndex();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUComputePassTimestampWrites.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUComputePassTimestampWrites.querySet(segment, querySet().segment());
        WGPUComputePassTimestampWrites.beginningOfPassWriteIndex(segment, beginningOfPassWriteIndex().orElse(WGPU_QUERY_SET_INDEX_UNDEFINED()));
        WGPUComputePassTimestampWrites.endOfPassWriteIndex(segment, endOfPassWriteIndex().orElse(WGPU_QUERY_SET_INDEX_UNDEFINED()));
    }
}
