package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUShaderDefine;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

@WgpuStyle
@Value.Immutable
public interface ShaderDefine extends WgpuStruct {
    String name();

    String value();

    @Value.NonAttribute
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUShaderDefine.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUShaderDefine.name(segment), name());
        WgpuUtils.setString(allocator, WGPUShaderDefine.value(segment), value());
    }
}
