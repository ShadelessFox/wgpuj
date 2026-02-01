package sh.adelessfox.wgpu;

import sh.adelessfox.wgpu.util.WgpuObject;
import sh.adelessfox.wgpu.util.WgpuUtils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.util.List;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record RenderPass(MemorySegment segment) implements WgpuObject {
    public void beginOcclusionQuery(int queryIndex) {
        wgpuRenderPassEncoderBeginOcclusionQuery(segment, queryIndex);
    }

    public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
        wgpuRenderPassEncoderDraw(segment, vertexCount, instanceCount, firstVertex, firstInstance);
    }

    public void drawIndexed(int indexCount, int instanceCount, int firstIndex, int baseVertex, int firstInstance) {
        wgpuRenderPassEncoderDrawIndexed(segment, indexCount, instanceCount, firstIndex, baseVertex, firstInstance);
    }

    public void drawIndexedIndirect(Buffer indirectBuffer, long indirectOffset) {
        wgpuRenderPassEncoderDrawIndexedIndirect(segment, indirectBuffer.segment(), indirectOffset);
    }

    public void drawIndirect(Buffer indirectBuffer, long indirectOffset) {
        wgpuRenderPassEncoderDrawIndirect(segment, indirectBuffer.segment(), indirectOffset);
    }

    public void end() {
        wgpuRenderPassEncoderEnd(segment);
    }

    public void endOcclusionQuery() {
        wgpuRenderPassEncoderEndOcclusionQuery(segment);
    }

    public void executeBundles(List<RenderBundle> bundles) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuRenderPassEncoderExecuteBundles(
                segment,
                bundles.size(),
                WgpuUtils.toNative(arena, ValueLayout.ADDRESS, bundles, RenderBundle::segment)
            );
        }
    }

    public void insertDebugMarker(String markerLabel) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuRenderPassEncoderInsertDebugMarker(segment, WgpuUtils.toNative(arena, markerLabel));
        }
    }

    public void popDebugGroup() {
        wgpuRenderPassEncoderPopDebugGroup(segment);
    }

    public void pushDebugGroup(String groupLabel) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuRenderPassEncoderPushDebugGroup(segment, WgpuUtils.toNative(arena, groupLabel));
        }
    }

    public void setBindGroup(int groupIndex, Optional<BindGroup> group, int[] dynamicOffsets) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuRenderPassEncoderSetBindGroup(
                segment,
                groupIndex,
                group.map(BindGroup::segment).orElse(MemorySegment.NULL),
                dynamicOffsets.length,
                arena.allocateFrom(ValueLayout.JAVA_INT, dynamicOffsets)
            );
        }
    }

    public void setBlendConstant(Color color) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuRenderPassEncoderSetBlendConstant(segment, color.toNative(arena));
        }
    }

    public void setIndexBuffer(Buffer buffer, IndexFormat format, long offset, long size) {
        wgpuRenderPassEncoderSetIndexBuffer(segment, buffer.segment(), format.value(), offset, size);
    }

    public void setLabel(String label) {
        try (Arena arena = Arena.ofConfined()) {
            wgpuRenderPassEncoderSetLabel(segment, WgpuUtils.toNative(arena, label));
        }
    }

    public void setPipeline(RenderPipeline pipeline) {
        wgpuRenderPassEncoderSetPipeline(segment, pipeline.segment());
    }

    public void setScissorRect(int x, int y, int width, int height) {
        wgpuRenderPassEncoderSetScissorRect(segment, x, y, width, height);
    }

    public void setStencilReference(int reference) {
        wgpuRenderPassEncoderSetStencilReference(segment, reference);
    }

    public void setVertexBuffer(int slot, Optional<Buffer> buffer, long offset, long size) {
        wgpuRenderPassEncoderSetVertexBuffer(
            segment,
            slot,
            buffer.map(Buffer::segment).orElse(MemorySegment.NULL),
            offset,
            size
        );
    }

    public void setViewport(float x, float y, float width, float height, float minDepth, float maxDepth) {
        wgpuRenderPassEncoderSetViewport(segment, x, y, width, height, minDepth, maxDepth);
    }

    @Override
    public void close() {
        wgpuRenderPassEncoderRelease(segment);
    }
}
