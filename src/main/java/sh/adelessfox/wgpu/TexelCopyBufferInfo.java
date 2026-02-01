package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu_native.WGPUTexelCopyBufferInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@Value.Builder
public record TexelCopyBufferInfo(
    TexelCopyBufferLayout layout,
    Buffer buffer
) implements WgpuStruct {
    public static TexelCopyBufferInfoBuilder builder() {
        return new TexelCopyBufferInfoBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUTexelCopyBufferInfo.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        layout.toNative(allocator, WGPUTexelCopyBufferInfo.layout(segment));
        WGPUTexelCopyBufferInfo.buffer(segment, buffer.segment());
    }
}
