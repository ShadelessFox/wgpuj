package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUFragmentState;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.util.List;

@WgpuStyle
@Value.Immutable
public interface FragmentState extends ProgrammableStage, WgpuStruct {
    List<ColorTargetState> targets();

    @Value.Derived
    @Override
    default MemoryLayout nativeLayout() {
        return WGPUFragmentState.layout();
    }

    @Override
    default void toNative(SegmentAllocator allocator, MemorySegment segment) {
        WGPUFragmentState.module(segment, module().segment());
        entryPoint().ifPresent(ep -> WgpuUtils.setString(allocator, WGPUFragmentState.entryPoint(segment), ep));
        WgpuUtils.setArray(allocator, segment, WGPUFragmentState.constantCount$offset(), constants());
        WgpuUtils.setArray(allocator, segment, WGPUFragmentState.targetCount$offset(), targets());
    }
}
