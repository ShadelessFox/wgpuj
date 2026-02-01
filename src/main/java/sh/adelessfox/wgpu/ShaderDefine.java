package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUShaderDefine;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public record ShaderDefine(String name, String value) {
    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUShaderDefine.allocate(allocator);
        WgpuUtils.setString(allocator, WGPUShaderDefine.name(segment), name);
        WgpuUtils.setString(allocator, WGPUShaderDefine.value(segment), value);
        return segment;
    }
}
