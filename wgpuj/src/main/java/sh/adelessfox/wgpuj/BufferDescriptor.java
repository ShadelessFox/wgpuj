package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBufferDescriptor;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.Set;

@WgpuStyle
@Value.Immutable
public interface BufferDescriptor extends ObjectDescriptorBase, WgpuStruct {
    long size();

    Set<BufferUsage> usages();

    default boolean mappedAtCreation() {
        return false;
    }

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUBufferDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        label().ifPresent(x -> WgpuUtils.setString(allocator, WGPUBufferDescriptor.label(segment), x));
        WGPUBufferDescriptor.usage(segment, WgpuFlags.toNative(usages()));
        WGPUBufferDescriptor.size(segment, size());
        WGPUBufferDescriptor.mappedAtCreation(segment, WgpuUtils.toNative(mappedAtCreation()));
    }
}
