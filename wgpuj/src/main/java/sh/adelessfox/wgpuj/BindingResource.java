package sh.adelessfox.wgpuj;

public sealed interface BindingResource {
    record Buffer(BufferBinding binding) implements BindingResource {
    }

    record Sampler(sh.adelessfox.wgpuj.Sampler sampler) implements BindingResource {
    }

    record TextureView(sh.adelessfox.wgpuj.TextureView view) implements BindingResource {
    }
}