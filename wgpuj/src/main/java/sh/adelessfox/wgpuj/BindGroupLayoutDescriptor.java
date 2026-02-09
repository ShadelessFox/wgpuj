package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUBindGroupLayoutDescriptor;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;

@WgpuStyle
@Value.Immutable
public interface BindGroupLayoutDescriptor extends ObjectDescriptorBase, WgpuStruct {
    List<BindGroupLayoutEntry> entries();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUBindGroupLayoutDescriptor.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUBindGroupLayoutDescriptor.label(segment), label());
        WgpuUtils.setArray(allocator, segment, WGPUBindGroupLayoutDescriptor.entryCount$offset(), entries());
    }
}
