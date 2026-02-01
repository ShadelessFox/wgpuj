package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.*;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.util.List;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public sealed interface ShaderSource {
    MemorySegment toNative(SegmentAllocator allocator);

    record SpirV(int[] code) implements ShaderSource {
        @Override
        public MemorySegment toNative(SegmentAllocator allocator) {
            var segment = WGPUShaderSourceSPIRV.allocate(allocator);
            WGPUChainedStruct.sType(WGPUShaderSourceSPIRV.chain(segment), WGPUSType_ShaderSourceSPIRV());
            WGPUShaderSourceSPIRV.codeSize(segment, code.length);
            WGPUShaderSourceSPIRV.code(segment, allocator.allocateFrom(ValueLayout.JAVA_INT, code));
            return segment;
        }
    }

    record Glsl(String code, Set<ShaderStage> stages, List<ShaderDefine> defines) implements ShaderSource {
        @Override
        public MemorySegment toNative(SegmentAllocator allocator) {
            var segment = WGPUShaderSourceGLSL.allocate(allocator);
            WGPUChainedStruct.sType(WGPUShaderSourceGLSL.chain(segment), WGPUSType_ShaderSourceGLSL());
            WGPUShaderSourceGLSL.stage(segment, WgpuUtils.toNative(stages));
            WgpuUtils.setString(allocator, WGPUShaderSourceWGSL.code(segment), code);
            WgpuUtils.setArray(allocator, segment, WGPUShaderSourceGLSL.defineCount$offset(), WGPUShaderDefine.layout(), defines, ShaderDefine::toNative);
            return segment;
        }
    }

    record Wgsl(String code) implements ShaderSource {
        @Override
        public MemorySegment toNative(SegmentAllocator allocator) {
            var segment = WGPUShaderSourceWGSL.allocate(allocator);
            WGPUChainedStruct.sType(WGPUShaderSourceWGSL.chain(segment), WGPUSType_ShaderSourceWGSL());
            WgpuUtils.setString(allocator, WGPUShaderSourceWGSL.code(segment), code);
            return segment;
        }
    }
}
