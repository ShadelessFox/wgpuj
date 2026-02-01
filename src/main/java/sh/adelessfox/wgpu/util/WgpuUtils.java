package sh.adelessfox.wgpu.util;

import sh.adelessfox.wgpu_native.WGPUStringView;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    public static <T> void setArray(
        SegmentAllocator allocator,
        MemorySegment segment,
        long offset,
        MemoryLayout layout,
        List<? extends T> list,
        BiFunction<T, SegmentAllocator, MemorySegment> mapper
    ) {
        segment.set(ValueLayout.JAVA_LONG, offset, list.size());
        segment.set(ValueLayout.ADDRESS, offset + 8, toNative(allocator, layout, list, mapper));
    }

    public static MemorySegment toNative(SegmentAllocator allocator, ByteBuffer buffer) {
        var src = MemorySegment.ofBuffer(buffer);
        var dst = allocator.allocate(src.byteSize());
        return dst.copyFrom(src);
    }

    public static <T> MemorySegment toNative(
        SegmentAllocator allocator,
        MemoryLayout layout,
        List<? extends T> list,
        Function<T, MemorySegment> mapper
    ) {
        return toNative(
            allocator,
            layout,
            list,
            (t, _) -> mapper.apply(t)
        );
    }

    public static <T> MemorySegment toNative(
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

    public static MemorySegment toNative(SegmentAllocator allocator, String text) {
        var segment = WGPUStringView.allocate(allocator);
        setString(allocator, segment, text);
        return segment;
    }

    public static <T extends Enum<T> & WgpuFlags<T>> int toNative(Collection<? extends WgpuFlags<T>> e) {
        int v = 0;
        for (WgpuFlags<T> item : e) {
            v |= item.value();
        }
        return v;
    }

    public static int toNative(boolean value) {
        return value ? 1 : 0;
    }
}
