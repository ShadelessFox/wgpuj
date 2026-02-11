package sh.adelessfox.wgpuj.objects;

import sh.adelessfox.wgpuj.*;
import sh.adelessfox.wgpuj.util.WgpuObject;
import sh.adelessfox.wgpuj.util.WgpuUtils;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Optional;

import static sh.adelessfox.wgpu_native.wgpu_h.*;

public record Device(MemorySegment segment) implements WgpuObject {
    public BindGroup createBindGroup(BindGroupDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new BindGroup(wgpuDeviceCreateBindGroup(segment, descriptor.toNative(arena)));
        }
    }

    public BindGroupLayout createBindGroupLayout(BindGroupLayoutDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new BindGroupLayout(wgpuDeviceCreateBindGroupLayout(segment, descriptor.toNative(arena)));
        }
    }

    public Buffer createBuffer(BufferDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new Buffer(this, wgpuDeviceCreateBuffer(segment, descriptor.toNative(arena)));
        }
    }

    public CommandEncoder createCommandEncoder() {
        return createCommandEncoder(Optional.empty());
    }

    public CommandEncoder createCommandEncoder(CommandEncoderDescriptor descriptor) {
        return createCommandEncoder(Optional.of(descriptor));
    }

    private CommandEncoder createCommandEncoder(Optional<CommandEncoderDescriptor> descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new CommandEncoder(wgpuDeviceCreateCommandEncoder(
                segment,
                descriptor.map(d -> d.toNative(arena)).orElse(MemorySegment.NULL)));
        }
    }

    // ComputePipeline wgpuDeviceCreateComputePipeline(ComputePipelineDescriptor descriptor);

    public PipelineLayout createPipelineLayout(PipelineLayoutDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new PipelineLayout(wgpuDeviceCreatePipelineLayout(segment, descriptor.toNative(arena)));
        }
    }

    public QuerySet createQuerySet(QuerySetDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new QuerySet(wgpuDeviceCreateQuerySet(segment, descriptor.toNative(arena)));
        }
    }

    // RenderBundleEncoder wgpuDeviceCreateRenderBundleEncoder(RenderBundleEncoderDescriptor descriptor);

    public RenderPipeline createRenderPipeline(RenderPipelineDescriptor renderPipelineDescriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new RenderPipeline(wgpuDeviceCreateRenderPipeline(segment, renderPipelineDescriptor.toNative(arena)));
        }
    }

    // Sampler wgpuDeviceCreateSampler(Optional<SamplerDescriptor> descriptor);

    public ShaderModule createShaderModule(ShaderModuleDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new ShaderModule(wgpuDeviceCreateShaderModule(segment, descriptor.toNative(arena)));
        }
    }

    public Texture createTexture(TextureDescriptor descriptor) {
        try (Arena arena = Arena.ofConfined()) {
            return new Texture(wgpuDeviceCreateTexture(segment, descriptor.toNative(arena)));
        }
    }

    // AdapterInfo wgpuDeviceGetAdapterInfo();

    public boolean poll(boolean wait) {
        return wgpuDevicePoll(segment, WgpuUtils.toNative(wait), MemorySegment.NULL) == 1;
    }

    public Queue getQueue() {
        return new Queue(wgpuDeviceGetQueue(segment));
    }

    @Override
    public void close() {
        wgpuDeviceRelease(segment);
    }
}
