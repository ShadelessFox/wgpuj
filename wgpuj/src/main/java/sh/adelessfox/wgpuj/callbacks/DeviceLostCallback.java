package sh.adelessfox.wgpuj.callbacks;

import sh.adelessfox.wgpuj.util.WgpuEnum;

@FunctionalInterface
public interface DeviceLostCallback {
    void invoke(DeviceLostReason reason, String message);

    enum DeviceLostReason implements WgpuEnum<DeviceLostReason> {
        UNKNOWN(0x00000001),
        DESTROYED(0x00000002),
        INSTANCE_DROPPED(0x00000003),
        FAILED_CREATION(0x00000004);

        private final int value;

        DeviceLostReason(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }
    }
}
