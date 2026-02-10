package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpu_native.WGPUChainedStruct;
import sh.adelessfox.wgpu_native.WGPUSurfaceSourceWindowsHWND;
import sh.adelessfox.wgpuj.util.WgpuStruct;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPUSType_SurfaceSourceWindowsHWND;

@WgpuStyle
@Value.Enclosing
public sealed interface SurfaceSource extends WgpuStruct {
    @WgpuStyle
    @Value.Immutable
    non-sealed interface WindowsHwnd extends SurfaceSource {
        MemorySegment hinstance();

        MemorySegment hwnd();

        @Value.NonAttribute
        @Override
        default MemoryLayout nativeLayout() {
            return WGPUSurfaceSourceWindowsHWND.layout();
        }

        @Override
        default void toNative(SegmentAllocator allocator, MemorySegment segment) {
            WGPUChainedStruct.sType(WGPUSurfaceSourceWindowsHWND.chain(segment), WGPUSType_SurfaceSourceWindowsHWND());
            WGPUSurfaceSourceWindowsHWND.hinstance(segment, hinstance());
            WGPUSurfaceSourceWindowsHWND.hwnd(segment, hwnd());
        }
    }
}
