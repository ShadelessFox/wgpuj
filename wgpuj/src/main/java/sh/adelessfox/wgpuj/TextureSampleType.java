package sh.adelessfox.wgpuj;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public sealed interface TextureSampleType {
    record Float(boolean filterable) implements TextureSampleType {
    }

    record Depth() implements TextureSampleType {
    }

    record Sint() implements TextureSampleType {
    }

    record Uint() implements TextureSampleType {
    }

    default int toNative() {
        return switch (this) {
            case Float(var filterable) -> filterable
                ? WGPUTextureSampleType_Float()
                : WGPUTextureSampleType_UnfilterableFloat();
            case Depth _ -> WGPUTextureSampleType_Depth();
            case Sint _ -> WGPUTextureSampleType_Sint();
            case Uint _ -> WGPUTextureSampleType_Uint();
        };
    }
}
