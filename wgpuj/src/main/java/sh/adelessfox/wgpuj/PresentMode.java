package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.objects.Surface;
import sh.adelessfox.wgpuj.util.WgpuEnum;

/**
 * Describes when and in which order frames are presented on the
 * screen when {@link Surface#present()} is called.
 */
public enum PresentMode implements WgpuEnum<PresentMode> {
    /**
     * The presentation of the image to the user waits for
     * the next vertical blanking period to update in a
     * first-in, first-out manner.
     * <p>
     * Tearing cannot be observed and frame-loop will be
     * limited to the display's refresh rate.
     * <p>
     * This is the only mode that's always available.
     */
    FIFO(0x00000001),

    /**
     * The presentation of the image to the user tries to
     * wait for the next vertical blanking period but may
     * decide to not wait if a frame is presented late.
     * <p>
     * Tearing can sometimes be observed but late-frame
     * don't produce a full-frame stutter in the presentation.
     * <p>
     * This is still a first-in, first-out mechanism so a
     * frame-loop will be limited to the display's refresh rate.
     */
    FIFO_RELAXED(0x00000002),

    /**
     * The presentation of the image to the user is updated
     * immediately without waiting for a vertical blank.
     * <p>
     * Tearing can be observed but latency is minimized.
     */
    IMMEDIATE(0x00000003),

    /**
     * The presentation of the image to the user waits for the
     * next vertical blanking period to update to the latest
     * provided image.
     * <p>
     * Tearing cannot be observed and a frame-loop is not limited
     * o the display's refresh rate.
     */
    MAILBOX(0x00000004);

    private final int value;

    PresentMode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
