package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUTexelCopyBufferInfo;
import sh.adelessfox.wgpuj.objects.Buffer;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface TexelCopyBufferInfo extends WgpuStruct {
    TexelCopyBufferLayout layout();

    Buffer buffer();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUTexelCopyBufferInfo.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        layout().toNative(allocator, WGPUTexelCopyBufferInfo.layout(segment));
        WGPUTexelCopyBufferInfo.buffer(segment, buffer().segment());
    }
}
