package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuObject;
import sh.adelessfox.wgpu.util.WgpuUtils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record CommandEncoder(MemorySegment segment) implements WgpuObject {
    public ComputePass beginComputePass(Optional<ComputePassDescriptor> descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new ComputePass(wgpuCommandEncoderBeginComputePass(
                segment,
                descriptor.map(d -> d.toNative(arena)).orElse(MemorySegment.NULL)
            ));
        }
    }

    public RenderPass beginRenderPass(RenderPassDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new RenderPass(wgpuCommandEncoderBeginRenderPass(segment, descriptor.toNative(arena)));
        }
    }

    public void clearBuffer(Buffer buffer, long offset, long size) {
        wgpuCommandEncoderClearBuffer(segment, buffer.segment(), offset, size);
    }

    public void copyBufferToBuffer(Buffer source, long sourceOffset, Buffer destination, long destinationOffset, long size) {
        wgpuCommandEncoderCopyBufferToBuffer(
            segment,
            source.segment(),
            sourceOffset,
            destination.segment(),
            destinationOffset,
            size
        );
    }

    public void copyBufferToTexture(TexelCopyBufferInfo source, TexelCopyTextureInfo destination, Extent3D copySize) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuCommandEncoderCopyBufferToTexture(
                segment,
                source.toNative(arena),
                destination.toNative(arena),
                copySize.toNative(arena)
            );
        }
    }

    public void copyTextureToBuffer(TexelCopyTextureInfo source, TexelCopyBufferInfo destination, Extent3D copySize) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuCommandEncoderCopyTextureToBuffer(
                segment,
                source.toNative(arena),
                destination.toNative(arena),
                copySize.toNative(arena)
            );
        }
    }

    public void copyTextureToTexture(TexelCopyTextureInfo source, TexelCopyTextureInfo destination, Extent3D copySize) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuCommandEncoderCopyTextureToTexture(
                segment,
                source.toNative(arena),
                destination.toNative(arena),
                copySize.toNative(arena)
            );
        }
    }

    public CommandBuffer finish(Optional<CommandBufferDescriptor> descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new CommandBuffer(wgpuCommandEncoderFinish(
                segment,
                descriptor.map(d -> d.toNative(arena)).orElse(MemorySegment.NULL)
            ));
        }
    }

    public void insertDebugMarker(String markerLabel) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuCommandEncoderInsertDebugMarker(segment, WgpuUtils.toNative(arena, markerLabel));
        }
    }

    public void popDebugGroup() {
        wgpuCommandEncoderPopDebugGroup(segment);
    }

    public void pushDebugGroup(String groupLabel) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuCommandEncoderPushDebugGroup(segment, WgpuUtils.toNative(arena, groupLabel));
        }
    }

    public void resolveQuerySet(QuerySet querySet, int firstQuery, int queryCount, Buffer destination, long destinationOffset) {
        wgpuCommandEncoderResolveQuerySet(
            segment,
            querySet.segment(),
            firstQuery,
            queryCount,
            destination.segment(),
            destinationOffset
        );
    }

    public void setLabel(String label) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuCommandEncoderSetLabel(segment, WgpuUtils.toNative(arena, label));
        }
    }

    public void writeTimestamp(QuerySet querySet, int queryIndex) {
        wgpuCommandEncoderWriteTimestamp(segment, querySet.segment(), queryIndex);
    }

    @Override
    public void close() {
        wgpuCommandEncoderRelease(segment);
    }
}
