package sh.adelessfox.wgpuj;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public sealed interface BufferBindingType {
    record Uniform() implements BufferBindingType {
    }

    record Storage(boolean readOnly) implements BufferBindingType {
    }

    default int toNative() {
        return switch (this) {
            case Uniform _ -> WGPUBufferBindingType_Uniform();
            case Storage(var readOnly) -> readOnly
                ? WGPUBufferBindingType_ReadOnlyStorage()
                : WGPUBufferBindingType_Storage();
        };
    }
}
