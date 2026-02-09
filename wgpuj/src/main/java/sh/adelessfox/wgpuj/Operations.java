package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpuj.util.WgpuEnum;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.lang.foreign.MemorySegment;
import java.util.function.BiConsumer;

import static sh.adelessfox.wgpu_native.wgpu_h.WGPULoadOp_Clear;
import static sh.adelessfox.wgpu_native.wgpu_h.WGPULoadOp_Load;

@WgpuStyle
@Value.Enclosing
@Value.Immutable
public interface Operations<T> {
    @WgpuStyle
    @SuppressWarnings("unused")
    sealed interface LoadOp<T> {
        @WgpuStyle
        @Value.Immutable(builder = false, copy = false)
        non-sealed interface Clear<T> extends LoadOp<T> {
            T value();
        }

        @WgpuStyle
        @Value.Immutable(singleton = true, builder = false, copy = false)
        non-sealed interface Load<T> extends LoadOp<T> {
        }
    }

    enum StoreOp implements WgpuEnum<StoreOp> {
        STORE(0x00000001),
        DISCARD(0x00000002);

        private final int value;

        StoreOp(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }

    LoadOp<T> load();

    StoreOp store();

    default void toNative(
        MemorySegment segment,
        BiConsumer<MemorySegment, Integer> loadOp,
        BiConsumer<MemorySegment, Integer> storeOp,
        BiConsumer<MemorySegment, T> clearValue
    ) {
        switch (load()) {
            case LoadOp.Clear<T> clear -> {
                clearValue.accept(segment, clear.value());
                loadOp.accept(segment, WGPULoadOp_Clear());
            }
            case LoadOp.Load<T> _ -> loadOp.accept(segment, WGPULoadOp_Load());
        }
        storeOp.accept(segment, store().value());
    }
}
