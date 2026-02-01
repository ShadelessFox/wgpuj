package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuFlags;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUChainedStruct;
import sh.adelessfox.wgpu_native.WGPUShaderSourceGLSL;
import sh.adelessfox.wgpu_native.WGPUShaderSourceSPIRV;
import sh.adelessfox.wgpu_native.WGPUShaderSourceWGSL;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.util.List;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public sealed interface ShaderSource extends WgpuStruct {
    record SpirV(int[] code) implements ShaderSource {
        @Override
        public MemoryLayout nativeLayout() {
            return WGPUShaderSourceSPIRV.layout();
        }

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
        public MemoryLayout nativeLayout() {
            return WGPUShaderSourceGLSL.layout();
        }

        @Override
        public MemorySegment toNative(SegmentAllocator allocator) {
            var segment = WGPUShaderSourceGLSL.allocate(allocator);
            WGPUChainedStruct.sType(WGPUShaderSourceGLSL.chain(segment), WGPUSType_ShaderSourceGLSL());
            WGPUShaderSourceGLSL.stage(segment, WgpuFlags.toNative(stages));
            WgpuUtils.setString(allocator, WGPUShaderSourceGLSL.code(segment), code);
            WgpuUtils.setArray(allocator, segment, WGPUShaderSourceGLSL.defineCount$offset(), defines);
            return segment;
        }
    }

    record Wgsl(String code) implements ShaderSource {
        @Override
        public MemoryLayout nativeLayout() {
            return WGPUShaderSourceWGSL.layout();
        }

        @Override
        public MemorySegment toNative(SegmentAllocator allocator) {
            var segment = WGPUShaderSourceWGSL.allocate(allocator);
            WGPUChainedStruct.sType(WGPUShaderSourceWGSL.chain(segment), WGPUSType_ShaderSourceWGSL());
            WgpuUtils.setString(allocator, WGPUShaderSourceWGSL.code(segment), code);
            return segment;
        }
    }
}
