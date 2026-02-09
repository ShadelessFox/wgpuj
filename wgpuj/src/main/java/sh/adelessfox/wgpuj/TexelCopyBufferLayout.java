package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTexelCopyBufferLayout;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface TexelCopyBufferLayout extends WgpuStruct {
    default long offset() {
        return 0;
    }

    int bytesPerRow();

    int rowsPerImage();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUTexelCopyBufferLayout.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUTexelCopyBufferLayout.offset(segment, offset());
        WGPUTexelCopyBufferLayout.bytesPerRow(segment, bytesPerRow());
        WGPUTexelCopyBufferLayout.rowsPerImage(segment, rowsPerImage());
    }
}
