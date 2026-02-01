package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuFlags;
import sh.adelessfox.wgpu.util.WgpuUtils;
import sh.adelessfox.wgpu_native.WGPUBufferMapCallback;
import sh.adelessfox.wgpu_native.WGPUBufferMapCallbackInfo;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.Set;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Buffer(MemorySegment segment) implements AutoCloseable {
    public interface Mapped extends AutoCloseable {
        ByteBuffer getMappedRange(long offset, long size);

        @Override
        void close();
    }

    public Mapped map(Instance instance, long offset, long size, MapMode... modes) {
        var mode = Set.of(modes);
        try (Arena arena = Arena.ofConfined()) {
            var complete = new boolean[1];
            var callback = WGPUBufferMapCallbackInfo.allocate(arena);
            WGPUBufferMapCallbackInfo.callback(callback, WGPUBufferMapCallback.allocate((status, _, _, _) -> {
                if (status != WGPUMapAsyncStatus_Success()) {
                    throw new IllegalStateException("Failed to map async");
                }
                complete[0] = true;
            }, arena));
            wgpuBufferMapAsync(arena, segment, WgpuUtils.toNative(mode), offset, size, callback);
            while (!complete[0]) {
                wgpuInstanceProcessEvents(instance.segment());
            }
        }
        return new Mapped() {
            @Override
            public ByteBuffer getMappedRange(long offset, long size) {
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
