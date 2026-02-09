package sh.adelessfox.wgpuj;

import org.immutables.value.Value;
import sh.adelessfox.wgpuj.util.WgpuStyle;

@WgpuStyle
@Value.Immutable(singleton = true)
public interface StencilState {
    default StencilFaceState front() {
        return ImmutableStencilFaceState.of();
    }

    default StencilFaceState back() {
        return ImmutableStencilFaceState.of();
    }

    default int readMask() {
        return 0xFFFFFFFF;
    }

    default int writeMask() {
        return 0xFFFFFFFF;
    }
}
