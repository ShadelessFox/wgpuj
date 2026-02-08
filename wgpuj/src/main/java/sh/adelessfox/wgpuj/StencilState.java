package sh.adelessfox.wgpuj;

import org.immutables.value.Value;

import java.util.OptionalInt;

@Value.Builder
public record StencilState(
    StencilFaceState front,
    StencilFaceState back,
    OptionalInt readMask,
    OptionalInt writeMask
) {
    public static StencilStateBuilder builder() {
        return new StencilStateBuilder();
    }
}
