package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuEnum;

public enum VertexFormat implements WgpuEnum<VertexFormat> {
    UINT_8(0x00000001),
    UINT_8x2(0x00000002),
    UINT_8x4(0x00000003),
    SINT_8(0x00000004),
    SINT_8x2(0x00000005),
    SINT_8x4(0x00000006),
    UNORM_8(0x00000007),
    UNORM_8x2(0x00000008),
    UNORM_8x4(0x00000009),
    SNORM_8(0x0000000A),
    SNORM_8x2(0x0000000B),
    SNORM_8x4(0x0000000C),
    UINT_16(0x0000000D),
    UINT_16x2(0x0000000E),
    UINT_16x4(0x0000000F),
    SINT_16(0x00000010),
    SINT_16x2(0x00000011),
    SINT_16x4(0x00000012),
    UNORM_16(0x00000013),
    UNORM_16x2(0x00000014),
    UNORM_16x4(0x00000015),
    SNORM_16(0x00000016),
    SNORM_16x2(0x00000017),
    SNORM_16x4(0x00000018),
    FLOAT_16(0x00000019),
    FLOAT_16x2(0x0000001A),
    FLOAT_16x4(0x0000001B),
    FLOAT_32(0x0000001C),
    FLOAT_32x2(0x0000001D),
    FLOAT_32x3(0x0000001E),
    FLOAT_32x4(0x0000001F),
    UINT_32(0x00000020),
    UINT_32x2(0x00000021),
    UINT_32x3(0x00000022),
    UINT_32x4(0x00000023),
    SINT_32(0x00000024),
    SINT_32x2(0x00000025),
    SINT_32x3(0x00000026),
    SINT_32x4(0x00000027),
    UNORM_10_10_10_2(0x00000028),
    UNORM_8x4BGRA(0x00000029);

    private final int value;

    VertexFormat(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }
}
