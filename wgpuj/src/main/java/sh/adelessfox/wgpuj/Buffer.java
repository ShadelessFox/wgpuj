package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpu_native.WGPUBufferMapCallback;
import sh.adelessfox.wgpu_native.WGPUBufferMapCallbackInfo;
import sh.adelessfox.wgpuj.util.WgpuFlags;
import sh.adelessfox.wgpuj.util.WgpuObject;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Buffer(Device device, MemorySegment segment) implements WgpuObject {
    public interface Mapped extends AutoCloseable {
        ByteBuffer asBuffer(long offset, long size);

        void get(ByteBuffer dst, long offset);

        void put(ByteBuffer src, long offset);

        @Override
        void close();
    }

    public Mapped map(long offset, long size, MapMode... modes) {
        if (modes.length == 0) {
            throw new IllegalArgumentException("At least one map mode must be specified");
        }
        var mode = Set.of(modes);
        try (Arena arena = Arena.ofConfined()) {
            var complete = new boolean[1];
            var callback = WGPUBufferMapCallbackInfo.allocate(arena);
            WGPUBufferMapCallbackInfo.callback(callback, WGPUBufferMapCallback.allocate((status, message, _, _) -> {
                if (status != WGPUMapAsyncStatus_Success()) {
                    throw new IllegalStateException("Failed to map buffer: " + WgpuUtils.getString(message));
                }
                complete[0] = true;
            }, arena));
            wgpuBufferMapAsync(arena, segment, WgpuFlags.toNative(mode), offset, size, callback);
            var instance = device.adapter().instance();
            while (!complete[0]) {
                wgpuInstanceProcessEvents(instance.segment());
            }
        }
        return new Mapped() {
            @Override
            public ByteBuffer asBuffer(long offset, long size) {
                if (mode.contains(MapMode.WRITE)) {
                    return wgpuBufferGetMappedRange(segment, offset, size)
                        .reinterpret(size)
                        .asByteBuffer();
                } else {
                    return wgpuBufferGetConstMappedRange(segment, offset, size)
                        .reinterpret(size)
                        .asByteBuffer()
                        .asReadOnlyBuffer();
                }
            }

            @Override
            public void get(ByteBuffer dst, long offset) {
                if (!mode.contains(MapMode.READ)) {
                    throw new IllegalStateException("Buffer not mapped for reading");
                }
                dst.put(asBuffer(offset, dst.remaining()));
            }

            @Override
            public void put(ByteBuffer src, long offset) {
                if (!mode.contains(MapMode.WRITE)) {
                    throw new IllegalStateException("Buffer not mapped for writing");
                }
                asBuffer(offset, src.remaining()).put(src);
            }

            @Override
            public void close() {
                wgpuBufferUnmap(segment);
            }
        };
    }

    public long getSize() {
        return wgpuBufferGetSize(segment);
    }

    public Set<BufferUsage> getUsage() {
        return WgpuFlags.setOf(wgpuBufferGetUsage(segment), BufferUsage.class);
    }

    public void setLabel(String label) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuBufferSetLabel(segment, WgpuUtils.toNative(arena, label));
        }
    }

    @Override
    public void close() {
        wgpuBufferRelease(segment);
    }
}
