package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTexelCopyBufferLayout;
import sh.adelessfox.wgpuj.util.WgpuStruct;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record TexelCopyBufferLayout(
    long offset,
    int bytesPerRow,
    int rowsPerImage
) implements WgpuStruct {
    public static TexelCopyBufferLayoutBuilder builder() {
        return new TexelCopyBufferLayoutBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUTexelCopyBufferLayout.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUTexelCopyBufferLayout.offset(segment, offset);
        WGPUTexelCopyBufferLayout.bytesPerRow(segment, bytesPerRow);
        WGPUTexelCopyBufferLayout.rowsPerImage(segment, rowsPerImage);
    }
}
