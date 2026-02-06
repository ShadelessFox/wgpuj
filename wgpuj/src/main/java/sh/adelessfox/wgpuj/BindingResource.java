package sh.adelessfox.wgpuj;

import java.util.List;

public sealed interface BindingResource {
    record Buffer(BufferBinding binding) implements BindingResource {
    }

    record BufferArray(List<BufferBinding> bindings) implements BindingResource {
        public BufferArray {
            bindings = List.copyOf(bindings);
        }
    }

    record Sampler(sh.adelessfox.wgpuj.Sampler sampler) implements BindingResource {
    }

    record SamplerArray(List<sh.adelessfox.wgpuj.Sampler> samplers) implements BindingResource {
        public SamplerArray {
            samplers = List.copyOf(samplers);
        }
    }

    record TextureView(TextureView view) implements BindingResource {
    }

    record TextureViewArray(List<TextureView> views) implements BindingResource {
        public TextureViewArray {
            views = List.copyOf(views);
        }
    }
}