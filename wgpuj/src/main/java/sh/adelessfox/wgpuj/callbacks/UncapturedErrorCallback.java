package sh.adelessfox.wgpuj.callbacks;

import sh.adelessfox.wgpuj.util.WgpuEnum;

@FunctionalInterface
public interface UncapturedErrorCallback {
    void invoke(ErrorType type, String message);

    enum ErrorType implements WgpuEnum<ErrorType> {
        NO_ERROR(0x00000001),
        VALIDATION(0x00000002),
        OUT_OF_MEMORY(0x00000003),
        INTERNAL(0x00000004),
        UNKNOWN(0x00000005);

        private final int value;

        ErrorType(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }
}
