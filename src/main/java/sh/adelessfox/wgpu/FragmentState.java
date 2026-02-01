package sh.adelessfox.wgpu;

import org.immutables.value.Value;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUColorTargetState;
import sh.adelessfox.wgpu_native.WGPUConstantEntry;
import sh.adelessfox.wgpu_native.WGPUFragmentState;

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
) {
    public static FragmentStateBuilder builder() {
        return new FragmentStateBuilder();
    }

    MemorySegment toNative(SegmentAllocator allocator) {
        var segment = WGPUFragmentState.allocate(allocator);
        WGPUFragmentState.module(segment, module.segment());
        entryPoint.ifPresent(ep -> WgpuUtils.setString(allocator, WGPUFragmentState.entryPoint(segment), ep));
        WgpuUtils.setArray(allocator, segment, WGPUFragmentState.constantCount$offset(), WGPUConstantEntry.layout(), constants, ConstantEntry::toNative);
        WgpuUtils.setArray(allocator, segment, WGPUFragmentState.targetCount$offset(), WGPUColorTargetState.layout(), targets, ColorTargetState::toNative);
        return segment;
    }
}
