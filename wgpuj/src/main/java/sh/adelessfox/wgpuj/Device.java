package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.util.WgpuObject;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Device(MemorySegment segment) implements WgpuObject {
    public Buffer createBuffer(BufferDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new Buffer(wgpuDeviceCreateBuffer(segment, descriptor.toNative(arena)));
        }
    }

    public Texture createTexture(TextureDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new Texture(wgpuDeviceCreateTexture(segment, descriptor.toNative(arena)));
        }
    }

    public ShaderModule createShaderModule(ShaderModuleDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new ShaderModule(wgpuDeviceCreateShaderModule(segment, descriptor.toNative(arena)));
        }
    }

    public CommandEncoder createCommandEncoder(CommandEncoderDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new CommandEncoder(wgpuDeviceCreateCommandEncoder(segment, descriptor.toNative(arena)));
        }
    }

    public RenderPipeline createRenderPipeline(RenderPipelineDescriptor renderPipelineDescriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new RenderPipeline(wgpuDeviceCreateRenderPipeline(segment, renderPipelineDescriptor.toNative(arena)));
        }
    }

    public Queue getQueue() {
        return new Queue(wgpuDeviceGetQueue(segment));
    }

    @Override
    public void close() {
        wgpuDeviceRelease(segment);
    }
}
