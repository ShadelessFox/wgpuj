package sh.adelessfox.wgpu.util;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public interface WgpuFlags<T extends Enum<T> & WgpuFlags<T>> extends WgpuEnum<T> {
    static <T extends Enum<T> & WgpuFlags<T>> Set<T> setOf(long value, Class<T> cls) {
        var values = new HashSet<T>();
        for (T constant : cls.getEnumConstants()) {
            if ((constant.value() & value) != 0) {
                value &= ~constant.value();
                values.add(constant);
            }
        }
        if (value != 0) {
            throw new IllegalArgumentException("Unknown flag bits: " + value);
        }
        return Set.copyOf(values);
    }
}
