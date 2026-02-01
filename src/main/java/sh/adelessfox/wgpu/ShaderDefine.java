package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUShaderDefine;

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
