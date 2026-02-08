package sh.adelessfox.wgpuj;

import java.util.OptionalLong;

public sealed interface BindingResource {
    record Buffer(sh.adelessfox.wgpuj.Buffer buffer, long offset, OptionalLong size) implements BindingResource {
        public Buffer(sh.adelessfox.wgpuj.Buffer buffer) {
            this(buffer, 0L, OptionalLong.empty());
        }

        public Buffer(sh.adelessfox.wgpuj.Buffer buffer, long offset) {
            this(buffer, offset, OptionalLong.empty());
        }

        public Buffer(sh.adelessfox.wgpuj.Buffer buffer, long offset, long size) {
            this(buffer, offset, OptionalLong.of(size));
        }
    }

    record Sampler(sh.adelessfox.wgpuj.Sampler sampler) implements BindingResource {
    }

    record TextureView(sh.adelessfox.wgpuj.TextureView view) implements BindingResource {
    }
}