package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.objects.Surface;
import sh.adelessfox.wgpuj.util.WgpuEnum;

/**
 * Describes how frames are composited with other contents on the
 * screen when {@link Surface#present()} is called.
 */
public enum CompositeAlphaMode implements WgpuEnum<CompositeAlphaMode> {
    /**
     * Lets the WebGPU implementation choose the best mode
     * (supported, and with the best performance) between
     * {@link #OPAQUE} or {@link #INHERIT}.
     */
    AUTO(0x00000000),

    /**
     * The alpha component of the image is ignored and teated
     * as if it is always 1.0.
     */
    OPAQUE(0x00000001),

    /**
     * The alpha component is respected and non-alpha components
     * are assumed to be already multiplied with the alpha component.
     * <p>
     * For example, (0.5, 0, 0, 0.5) is semi-transparent bright red.
     */
    PREMULTIPLIED(0x00000002),

    /**
     * The alpha component is respected and non-alpha components
     * are assumed to NOT be already multiplied with the alpha
     * component.
     * <p>
     * For example, (1.0, 0, 0, 0.5) is semi-transparent bright red.
     */
    UNPREMULTIPLIED(0x00000003),

    /**
     * The handling of the alpha component is unknown to WebGPU
     * and should be handled by the application using system-specific
     * APIs. This mode may be unavailable (for example on Wasm).
     */
    INHERIT(0x00000004);

    private final int value;

    CompositeAlphaMode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
