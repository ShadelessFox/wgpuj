package sh.adelessfox.demo;

import sh.adelessfox.wgpu_native.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.file.Files;
import java.nio.file.Path;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public class MainRaw {
    static void main() throws IOException {
        System.loadLibrary("lib/wgpu_native");

        try (Arena arena = Arena.ofConfined()) {
            var instanceDescriptor = WGPUInstanceDescriptor.allocate(arena);
            var instance = wgpuCreateInstance(instanceDescriptor);

            var adapterOptions = WGPURequestAdapterOptions.allocate(arena);
            var adapter = requestAdapter(instance, adapterOptions);

            var deviceDescriptor = WGPUDeviceDescriptor.allocate(arena);
            var device = requestDevice(adapter, deviceDescriptor);

            var bufferDescriptor = WGPUBufferDescriptor.allocate(arena);
            WGPUBufferDescriptor.usage(bufferDescriptor, 0x9 /* MapRead | CopyDst */);
            WGPUBufferDescriptor.size(bufferDescriptor, 512 * 512 * 4);

            var buffer = wgpuDeviceCreateBuffer(device, bufferDescriptor);

            var queue = wgpuDeviceGetQueue(device);
            var shaderModule = createShaderModule(device, Path.of("src/main/resources/main.wgsl"));

            var texture = createTexture(device, 512, 512, WGPUTextureFormat_RGBA8UnormSrgb());
            var textureView = wgpuTextureCreateView(texture, MemorySegment.NULL);
            var renderPipeline = createRenderPipeline(device, shaderModule, WGPUTextureFormat_RGBA8UnormSrgb());

            {
                var colorAttachment = WGPURenderPassColorAttachment.allocate(arena);
                WGPURenderPassColorAttachment.view(colorAttachment, textureView);
                WGPURenderPassColorAttachment.depthSlice(colorAttachment, WGPU_DEPTH_SLICE_UNDEFINED());
                WGPURenderPassColorAttachment.loadOp(colorAttachment, WGPULoadOp_Clear());
                WGPURenderPassColorAttachment.storeOp(colorAttachment, WGPUStoreOp_Store());

                var colorAttachmentColor = WGPURenderPassColorAttachment.clearValue(colorAttachment);
                WGPUColor.r(colorAttachmentColor, 0.0);
                WGPUColor.g(colorAttachmentColor, 1.0);
                WGPUColor.b(colorAttachmentColor, 0.0);
                WGPUColor.a(colorAttachmentColor, 1.0);

                var commandEncoderDescriptor = WGPUCommandEncoderDescriptor.allocate(arena);
                var commandEncoder = wgpuDeviceCreateCommandEncoder(device, commandEncoderDescriptor);

                var renderPassDescriptor = WGPURenderPassDescriptor.allocate(arena);
                WGPURenderPassDescriptor.colorAttachmentCount(renderPassDescriptor, 1);
                WGPURenderPassDescriptor.colorAttachments(renderPassDescriptor, colorAttachment);

                var renderPassEncoder = wgpuCommandEncoderBeginRenderPass(commandEncoder, renderPassDescriptor);

                wgpuRenderPassEncoderSetPipeline(renderPassEncoder, renderPipeline);
                wgpuRenderPassEncoderDraw(renderPassEncoder, 3, 1, 0, 0);
                wgpuRenderPassEncoderEnd(renderPassEncoder);
                wgpuRenderPassEncoderRelease(renderPassEncoder);

                {
                    var source = WGPUTexelCopyTextureInfo.allocate(arena);
                    WGPUTexelCopyTextureInfo.texture(source, texture);
                    WGPUTexelCopyTextureInfo.aspect(source, WGPUTextureAspect_All());

                    var destination = WGPUTexelCopyBufferInfo.allocate(arena);
                    WGPUTexelCopyBufferInfo.buffer(destination, buffer);

                    var destinationLayout = WGPUTexelCopyBufferInfo.layout(destination);
                    WGPUTexelCopyBufferLayout.bytesPerRow(destinationLayout, 512 * 4);
                    WGPUTexelCopyBufferLayout.rowsPerImage(destinationLayout, 512);

                    var size = WGPUExtent3D.allocate(arena);
                    WGPUExtent3D.width(size, 512);
                    WGPUExtent3D.height(size, 512);
                    WGPUExtent3D.depthOrArrayLayers(size, 1);

                    wgpuCommandEncoderCopyTextureToBuffer(commandEncoder, source, destination, size);
                }

                var commandBufferDescriptor = WGPUCommandBufferDescriptor.allocate(arena);
                var commandBuffer = wgpuCommandEncoderFinish(commandEncoder, commandBufferDescriptor);

                wgpuQueueSubmit(queue, 1, arena.allocateFrom(ValueLayout.ADDRESS, commandBuffer));

                wgpuCommandBufferRelease(commandBuffer);
                wgpuCommandEncoderRelease(commandEncoder);

                var callback = WGPUBufferMapCallbackInfo.allocate(arena);
                WGPUBufferMapCallbackInfo.callback(callback, WGPUBufferMapCallback.allocate((status, message, userdata1, userdata2) -> {
                    var data = wgpuBufferGetMappedRange(buffer, 0, 512 * 512 * 4)
                        .reinterpret(512 * 512 * 4)
                        .asByteBuffer();

                    var image = new BufferedImage(512, 512, BufferedImage.TYPE_4BYTE_ABGR);
                    var imageBuffer = (DataBufferByte) image.getRaster().getDataBuffer();

                    data.get(imageBuffer.getData());

                    System.out.println("yeet");
                }, arena));

                wgpuBufferMapAsync(arena, buffer, 0x1 /* Read */, 0, 512 * 512 * 4, callback);
                wgpuDevicePoll(device, 0, MemorySegment.NULL);
            }

            wgpuRenderPipelineRelease(renderPipeline);
            wgpuTextureViewRelease(textureView);
            wgpuTextureRelease(texture);
            wgpuShaderModuleRelease(shaderModule);
            wgpuQueueRelease(queue);
            wgpuDeviceRelease(device);
            wgpuAdapterRelease(adapter);
            wgpuInstanceRelease(instance);
        }
    }

    private static MemorySegment requestAdapter(MemorySegment instance, MemorySegment options) {
        try (Arena arena = Arena.ofConfined()) {
            var result = arena.allocate(ValueLayout.ADDRESS);

            var callback = WGPURequestAdapterCallbackInfo.allocate(arena);
            WGPURequestAdapterCallbackInfo.callback(callback, WGPURequestAdapterCallback.allocate(MainRaw::requestAdapterCallback, arena));
            WGPURequestAdapterCallbackInfo.userdata1(callback, result);

            wgpuInstanceRequestAdapter(arena, instance, options, callback);

            return result.get(ValueLayout.ADDRESS, 0);
        }
    }

    private static void requestAdapterCallback(int status, MemorySegment adapter, MemorySegment message, MemorySegment userdata1, MemorySegment userdata2) {
        if (status != WGPURequestAdapterStatus_Success()) {
            throw new IllegalStateException();
        }
        userdata1.reinterpret(8).set(ValueLayout.ADDRESS, 0, adapter);
    }

    private static MemorySegment requestDevice(MemorySegment adapter, MemorySegment descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            var result = arena.allocate(ValueLayout.ADDRESS);

            var callback = WGPURequestDeviceCallbackInfo.allocate(arena);
            WGPURequestDeviceCallbackInfo.callback(callback, WGPURequestDeviceCallback.allocate(MainRaw::requestDeviceCallback, arena));
            WGPURequestDeviceCallbackInfo.userdata1(callback, result);

            wgpuAdapterRequestDevice(arena, adapter, descriptor, callback);

            return result.get(ValueLayout.ADDRESS, 0);
        }
    }

    private static void requestDeviceCallback(int status, MemorySegment device, MemorySegment message, MemorySegment userdata1, MemorySegment userdata2) {
        if (status != WGPURequestDeviceStatus_Success()) {
            throw new IllegalStateException();
        }
        userdata1.reinterpret(8).set(ValueLayout.ADDRESS, 0, device);
    }

    private static MemorySegment createShaderModule(MemorySegment device, Path path) throws IOException {
        var code = Files.readString(path);

        try (Arena arena = Arena.ofConfined()) {
            var source = WGPUShaderSourceWGSL.allocate(arena);
            WGPUChainedStruct.sType(WGPUShaderSourceWGSL.chain(source), WGPUSType_ShaderSourceWGSL());
            WGPUStringView.data(WGPUShaderSourceWGSL.code(source), arena.allocateFrom(code));
            WGPUStringView.length(WGPUShaderSourceWGSL.code(source), code.length());

            var descriptor = WGPUShaderModuleDescriptor.allocate(arena);
            WGPUShaderModuleDescriptor.nextInChain(descriptor, source);

            return wgpuDeviceCreateShaderModule(device, descriptor);
        }
    }

    private static MemorySegment createRenderPipeline(MemorySegment device, MemorySegment shaderModule, int targetFormat) {
        try (Arena arena = Arena.ofConfined()) {
            var fragmentStateTarget = WGPUColorTargetState.allocate(arena);
            WGPUColorTargetState.format(fragmentStateTarget, targetFormat);
            WGPUColorTargetState.writeMask(fragmentStateTarget, 0xF /* All */);

            var fragmentState = WGPUFragmentState.allocate(arena);
            WGPUFragmentState.module(fragmentState, shaderModule);
            WGPUFragmentState.targetCount(fragmentState, 1);
            WGPUFragmentState.targets(fragmentState, fragmentStateTarget);

            var fragmentStateEntryPoint = WGPUFragmentState.entryPoint(fragmentState);
            WGPUStringView.data(fragmentStateEntryPoint, arena.allocateFrom("fs_main"));
            WGPUStringView.length(fragmentStateEntryPoint, "fs_main".length());

            var pipelineLayoutDescriptor = WGPUPipelineLayoutDescriptor.allocate(arena);
            var pipelineLayout = wgpuDeviceCreatePipelineLayout(device, pipelineLayoutDescriptor);

            var renderPipelineDescriptor = WGPURenderPipelineDescriptor.allocate(arena);
            WGPURenderPipelineDescriptor.layout(renderPipelineDescriptor, pipelineLayout);
            WGPURenderPipelineDescriptor.fragment(renderPipelineDescriptor, fragmentState);

            var renderPipelineDescriptorVertex = WGPURenderPipelineDescriptor.vertex(renderPipelineDescriptor);
            WGPUVertexState.module(renderPipelineDescriptorVertex, shaderModule);
            WGPUStringView.data(WGPUVertexState.entryPoint(renderPipelineDescriptorVertex), arena.allocateFrom("vs_main"));
            WGPUStringView.length(WGPUVertexState.entryPoint(renderPipelineDescriptorVertex), "vs_main".length());

            WGPUPrimitiveState.topology(WGPURenderPipelineDescriptor.primitive(renderPipelineDescriptor), WGPUPrimitiveTopology_TriangleList());
            WGPUMultisampleState.count(WGPURenderPipelineDescriptor.multisample(renderPipelineDescriptor), 1);
            WGPUMultisampleState.mask(WGPURenderPipelineDescriptor.multisample(renderPipelineDescriptor), 0xFFFFFFFF);

            try {
                return wgpuDeviceCreateRenderPipeline(device, renderPipelineDescriptor);
            } finally {
                wgpuPipelineLayoutRelease(pipelineLayout);
            }
        }
    }

    private static MemorySegment createTexture(MemorySegment device, int width, int height, int format) {
        try (Arena arena = Arena.ofConfined()) {
            var descriptor = WGPUTextureDescriptor.allocate(arena);
            WGPUTextureDescriptor.usage(descriptor, 0x11 /* CopySrc | RenderAttachment */);
            WGPUTextureDescriptor.dimension(descriptor, WGPUTextureDimension_2D());
            WGPUTextureDescriptor.format(descriptor, format);
            WGPUTextureDescriptor.mipLevelCount(descriptor, 1);
            WGPUTextureDescriptor.sampleCount(descriptor, 1);

            var descriptorSize = WGPUTextureDescriptor.size(descriptor);
            WGPUExtent3D.width(descriptorSize, width);
            WGPUExtent3D.height(descriptorSize, height);
            WGPUExtent3D.depthOrArrayLayers(descriptorSize, 1);

            return wgpuDeviceCreateTexture(device, descriptor);
        }
    }
}
