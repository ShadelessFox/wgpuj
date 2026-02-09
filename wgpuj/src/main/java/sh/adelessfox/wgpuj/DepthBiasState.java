package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpuj.util.WgpuStyle;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface DepthBiasState {
    default int constant() {
        return 0;
    }

    default float slopeScale() {
        return 0.0f;
    }

    default float clamp() {
        return 0.0f;
    }
}
