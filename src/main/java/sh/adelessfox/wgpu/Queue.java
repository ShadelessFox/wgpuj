package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuObject;
import sh.adelessfox.wgpu.util.WgpuUtils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.List;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Queue(MemorySegment segment) implements WgpuObject {
    // public WGPUFuture wgpuQueueOnSubmittedWorkDone(WGPUQueueWorkDoneCallbackInfo callbackInfo);

    public void setLabel(String label) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuQueueSetLabel(segment, WgpuUtils.toNative(arena, label));
        }
    }

    public void submit(List<CommandBuffer> commands) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuQueueSubmit(
                segment,
                commands.size(),
                WgpuUtils.toNative(arena, commands)
            );
        }
    }

    public void writeBuffer(Buffer buffer, long bufferOffset, ByteBuffer data) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuQueueWriteBuffer(
                segment,
                buffer.segment(),
                bufferOffset,
                WgpuUtils.toNative(arena, data),
                data.remaining()
            );
        }
    }

    public void writeTexture(TexelCopyTextureInfo destination, ByteBuffer data, TexelCopyBufferLayout dataLayout, Extent3D writeSize) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuQueueWriteTexture(
                segment,
                destination.toNative(arena),
                WgpuUtils.toNative(arena, data),
                data.remaining(),
                dataLayout.toNative(arena),
                writeSize.toNative(arena)
            );
        }
    }

    @Override
    public void close() {
        wgpuQueueRelease(segment);
    }
}
