package sh.adelessfox.wgpuj;

import java.lang.foreign.MemorySegment;
import java.util.Optional;
import java.util.function.BiConsumer;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Operations<T>(
    LoadOp<T> load,
    StoreOp store
) {
    static <T> void toNative(
        Optional<Operations<T>> ops,
        MemorySegment segment,
        BiConsumer<MemorySegment, Integer> loadOp,
        BiConsumer<MemorySegment, Integer> storeOp,
        BiConsumer<MemorySegment, T> clearValue
    ) {
        ops.ifPresentOrElse(
            ops1 -> ops1.toNative(segment, loadOp, storeOp, clearValue),
            () -> {
                loadOp.accept(segment, WGPULoadOp_Undefined());
                storeOp.accept(segment, WGPUStoreOp_Undefined());
            });
    }

    void toNative(
        MemorySegment segment,
        BiConsumer<MemorySegment, Integer> loadOp,
        BiConsumer<MemorySegment, Integer> storeOp,
        BiConsumer<MemorySegment, T> clearValue
    ) {
        switch (load) {
            case LoadOp.Clear<T>(var value) -> {
                clearValue.accept(segment, value);
                loadOp.accept(segment, WGPULoadOp_Clear());
            }
            case LoadOp.Load<T> _ -> loadOp.accept(segment, WGPULoadOp_Load());
        }
        storeOp.accept(segment, store.value());
    }
}
