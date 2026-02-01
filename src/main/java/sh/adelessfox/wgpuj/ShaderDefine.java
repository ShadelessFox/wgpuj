package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUShaderDefine;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record ShaderDefine(String name, String value) implements WgpuStruct {
    @Override
    public MemoryLayout nativeLayout() {
        return WGPUShaderDefine.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WgpuUtils.setString(allocator, WGPUShaderDefine.name(segment), name);
        WgpuUtils.setString(allocator, WGPUShaderDefine.value(segment), value);
    }
}
