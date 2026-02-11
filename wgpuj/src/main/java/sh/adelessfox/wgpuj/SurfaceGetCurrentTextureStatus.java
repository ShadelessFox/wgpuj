package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.objects.Device;
import sh.adelessfox.wgpuj.objects.Surface;
import sh.adelessfox.wgpuj.util.WgpuEnum;

/**
 * The status enum for {@link Surface#getCurrentTexture()}.
 */
public enum SurfaceGetCurrentTextureStatus implements WgpuEnum<SurfaceGetCurrentTextureStatus> {
    /**
     * Yay! Everything is good and we can render this frame.
     */
    SUCCESS_OPTIMAL(0x00000001),

    /**
     * Still OK - the surface can present the frame,
     * but in a suboptimal way.
     * <p>
     * The surface may need reconfiguration.
     */
    SUCCESS_SUBOPTIMAL(0x00000002),

    /**
     * Some operation timed out while trying to acquire the frame.
     */
    TIMEOUT(0x00000003),

    /**
     * The surface is too different to be used,
     * compared to when it was originally created.
     */
    OUTDATED(0x00000004),

    /**
     * The connection to whatever owns the surface was lost.
     */
    LOST(0x00000005),

    /**
     * The system ran out of memory.
     */
    OUT_OF_MEMORY(0x00000006),

    /**
     * The {@link Device} configured on the {@link Surface} was lost.
     */
    DEVICE_LOST(0x00000007),

    /**
     * The surface is not configured, or there was another error.
     */
    ERROR(0x00000008);

    private final int value;

    SurfaceGetCurrentTextureStatus(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
