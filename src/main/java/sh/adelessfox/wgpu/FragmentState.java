package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuStruct;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUFragmentState;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;
import java.util.Optional;

@Value.Builder
public record FragmentState(
    ShaderModule module,
    Optional<String> entryPoint,
    List<ConstantEntry> constants,
    List<ColorTargetState> targets
) implements WgpuStruct {
    public static FragmentStateBuilder builder() {
        return new FragmentStateBuilder();
    }

    @Override
    public MemoryLayout nativeLayout() {
        return WGPUFragmentState.layout();
    }

    @Override
    public void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUFragmentState.module(segment, module.segment());
        entryPoint.ifPresent(ep -> WgpuUtils.setString(allocator, WGPUFragmentState.entryPoint(segment), ep));
        WgpuUtils.setArray(allocator, segment, WGPUFragmentState.constantCount$offset(), constants);
        WgpuUtils.setArray(allocator, segment, WGPUFragmentState.targetCount$offset(), targets);
    }
}
