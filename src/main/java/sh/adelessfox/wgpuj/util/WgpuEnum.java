package sh.adelessfox.wgpuj.util;

@SuppressWarnings("unused")
public interface WgpuEnum<T extends Enum<T> & WgpuEnum<T>> {
    static <T extends Enum<T> & WgpuEnum<T>> T valueOf(int value, Class<T> cls) {
        for (T constant : cls.getEnumConstants()) {
            if (constant.value() == value) {
                return constant;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + value + " in " + cls.getName());
    }

    int value();
}
