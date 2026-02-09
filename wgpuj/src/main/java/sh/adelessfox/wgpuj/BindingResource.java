package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpuj.util.WgpuStyle;

import java.util.OptionalLong;

@WgpuStyle
@Value.Enclosing
public sealed interface BindingResource {
    @WgpuStyle
    @Value.Immutable
    non-sealed interface Buffer extends BindingResource {
        sh.adelessfox.wgpuj.objects.Buffer buffer();

        default long offset() {
            return 0;
        }

        OptionalLong size();
    }

    @WgpuStyle
    @Value.Immutable
    non-sealed interface Sampler extends BindingResource {
        sh.adelessfox.wgpuj.objects.Sampler sampler();
    }

    @WgpuStyle
    @Value.Immutable
    non-sealed interface TextureView extends BindingResource {
        sh.adelessfox.wgpuj.objects.TextureView view();
    }
}