package sh.adelessfox.wgpuj;

public sealed interface TextureSampleType {
    record Float(boolean filterable) implements TextureSampleType {
    }

    record Depth() implements TextureSampleType {
    }

    record Sint() implements TextureSampleType {
    }

    record Uint() implements TextureSampleType {
    }
}
