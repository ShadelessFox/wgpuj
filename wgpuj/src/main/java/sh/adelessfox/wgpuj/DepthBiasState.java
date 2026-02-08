package sh.adelessfox.wgpuj;

public record DepthBiasState(
    int constant,
    float slopeScale,
    float clamp
) {
}
