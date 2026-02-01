package sh.adelessfox.wgpuj.util;

import sh.adelessfox.wgpu_native.WGPUStringView;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

public final class WgpuUtils {
    private WgpuUtils() {
    }

    public static void setString(SegmentAllocator allocator, MemorySegment segment, String text) {
        WGPUStringView.data(segment, allocator.allocateFrom(text));
        WGPUStringView.length(segment, text.length());
    }

    public static void setString(SegmentAllocator allocator, MemorySegment segment, Optional<String> text) {
        text.ifPresent(t -> setString(allocator, segment, t));
    }

    public static <T extends WgpuStruct> void setArray(
        SegmentAllocator allocator,
        MemorySegment segment,
        long offset,
        List<? extends T> list
    ) {
        if (list.isEmpty()) {
            segment.set(ValueLayout.JAVA_LONG, offset, 0L);
            segment.set(ValueLayout.ADDRESS, offset + 8, MemorySegment.NULL);
        } else {
            var layout = list.getFirst().nativeLayout();
            segment.set(ValueLayout.JAVA_LONG, offset, list.size());
            segment.set(ValueLayout.ADDRESS, offset + 8, toNative(allocator, layout, list, T::toNative));
        }
    }

    public static <T extends WgpuObject> MemorySegment toNative(
        SegmentAllocator allocator,
        List<? extends T> list
    ) {
        return toNative(
            allocator,
            ValueLayout.ADDRESS,
            list,
            (t, a) -> a.allocateFrom(ValueLayout.ADDRESS, t.segment())
        );
    }

    private static <T> MemorySegment toNative(
        SegmentAllocator allocator,
        MemoryLayout layout,
        List<? extends T> list,
        BiFunction<T, SegmentAllocator, MemorySegment> mapper
    ) {
        if (list.isEmpty()) {
            return MemorySegment.NULL;
        }
        var items = allocator.allocate(layout, list.size());
        for (int i = 0; i < list.size(); i++) {
            var src = mapper.apply(list.get(i), allocator).reinterpret(layout.byteSize());
            var dst = items.asSlice(i * layout.byteSize(), layout.byteSize());
            dst.copyFrom(src);
        }
        return items;
    }

    public static MemorySegment toNative(SegmentAllocator allocator, ByteBuffer buffer) {
        var src = MemorySegment.ofBuffer(buffer);
        var dst = allocator.allocate(src.byteSize());
        return dst.copyFrom(src);
    }

    public static MemorySegment toNative(SegmentAllocator allocator, String text) {
        var segment = WGPUStringView.allocate(allocator);
        setString(allocator, segment, text);
        return segment;
    }

    public static int toNative(boolean value) {
        return value ? 1 : 0;
    }
}
