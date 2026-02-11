package sh.adelessfox.wgpuj.util;

import sh.adelessfox.wgpu_native.WGPUStringView;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;

public final class WgpuUtils {
    private WgpuUtils() {
    }

    public static String getString(MemorySegment segment) {
        var length = WGPUStringView.length(segment);
        var data = WGPUStringView.data(segment).reinterpret(length);
        return StandardCharsets.UTF_8.decode(data.asByteBuffer()).toString();
    }

    public static void setString(SegmentAllocator allocator, MemorySegment segment, String text) {
        WGPUStringView.data(segment, allocator.allocateFrom(text));
        WGPUStringView.length(segment, text.length());
    }

    public static <T> List<T> getArray(
        long count,
        MemorySegment entries,
        ValueLayout.OfInt layout,
        IntFunction<T> mapper
    ) {
        if (count == 0) {
            return List.of();
        }
        var out = new ArrayList<T>(Math.toIntExact(count));
        for (int i = 0; i < count; i++) {
            out.add(mapper.apply(entries.getAtIndex(layout, i)));
        }
        return List.copyOf(out);
    }

    public static <T extends WgpuStruct> void setArray(
        SegmentAllocator allocator,
        MemorySegment segment,
        List<? extends T> list,
        ObjIntConsumer<MemorySegment> countConsumer,
        BiConsumer<MemorySegment, MemorySegment> entriesConsumer
    ) {
        if (!list.isEmpty()) {
            var layout = list.getFirst().nativeLayout();
            countConsumer.accept(segment, list.size());
            entriesConsumer.accept(segment, toNative(allocator, layout, list, T::toNative));
        }
    }

    public static <T extends WgpuStruct> MemorySegment toNative(
        SegmentAllocator allocator,
        List<? extends T> list
    ) {
        if (list.isEmpty()) {
            return MemorySegment.NULL;
        }
        return toNative(
            allocator,
            list.getFirst().nativeLayout(),
            list,
            T::toNative
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
        if (buffer.isDirect()) {
            return src;
        } else {
            return allocator.allocate(src.byteSize()).copyFrom(src);
        }
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
