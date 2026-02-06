package sh.adelessfox.wgpuj;

public sealed interface BufferBindingType {
    record Uniform() implements BufferBindingType {
    }

    record Storage(boolean readOnly) implements BufferBindingType {
    }
}
