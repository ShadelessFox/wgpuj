package sh.adelessfox.wgpuj;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class App implements AutoCloseable {
    private final Instance instance;
    private final Adapter adapter;
    private final Device device;

    public App() {
        instance = Instance.create(InstanceDescriptor.builder()
            .addFlags(InstanceFlag.DEBUG, InstanceFlag.VALIDATION)
            .build());
        adapter = instance.requestAdapter();
        device = adapter.requestDevice(DeviceDescriptor.builder()
            .label("default device")
            .build());
    }

    void run() throws IOException {
        var queue = device.getQueue();

        var buffer = device.createBuffer(BufferDescriptor.builder()
            .label("buffer")
            .size(512 * 512 * 4)
            .addUsages(BufferUsage.COPY_DST, BufferUsage.MAP_READ)
            .mappedAtCreation(false)
            .build());

        var texture = device.createTexture(TextureDescriptor.builder()
            .label("texture")
            .size(new Extent3D(512, 512, 1))
            .mipLevelCount(1)
            .sampleCount(1)
            .dimension(TextureDimension.D2)
            .format(TextureFormat.RGBA8_UNORM)
            .addUsages(TextureUsage.COPY_SRC, TextureUsage.RENDER_ATTACHMENT)
            .build());

        var view = texture.createView();

        var module = device.createShaderModule(ShaderModuleDescriptor.builder()
            .label("shader")
            .source(new ShaderSource.Wgsl(Files.readString(Path.of("src/main/resources/main.wgsl"))))
            .build());

        var renderPipeline = createRenderPipeline(module, TextureFormat.RGBA8_UNORM);

        try (var encoder = device.createCommandEncoder(CommandEncoderDescriptor.builder().build())) {
            var descriptor = RenderPassDescriptor.builder()
                .addColorAttachments(RenderPassColorAttachment.builder()
                    .view(view)
                    .load(new LoadOp.Clear<>(new Color(0.2, 0.1, 0.7, 1.0)))
                    .store(StoreOp.STORE)
                    .build())
                .build();

            try (var pass = encoder.beginRenderPass(descriptor)) {
                pass.setPipeline(renderPipeline);
                pass.draw(3, 1, 0, 0);
                pass.end();
            }

            encoder.copyTextureToBuffer(
                TexelCopyTextureInfo.builder()
                    .texture(texture)
                    .mipLevel(0)
                    .origin(new Origin3D(0, 0, 0))
                    .aspect(TextureAspect.ALL)
                    .build(),
                TexelCopyBufferInfo.builder()
                    .buffer(buffer)
                    .layout(TexelCopyBufferLayout.builder()
                        .offset(0)
                        .bytesPerRow(512 * 4)
                        .rowsPerImage(512)
                        .build())
                    .build(),
                new Extent3D(512, 512, 1)
            );

            try (var encoded = encoder.finish(Optional.empty())) {
                queue.submit(List.of(encoded));
            }

            try (var mapped = buffer.map(instance, 0, 512 * 512 * 4, MapMode.READ)) {
                var image = new BufferedImage(512, 512, BufferedImage.TYPE_4BYTE_ABGR);
                var imageBuffer = (DataBufferByte) image.getRaster().getDataBuffer();

                var data = mapped.getMappedRange(0, 512 * 512 * 4);
                for (int i = 0; i < 512 * 512 * 4; i += 4) {
                    imageBuffer.getData()[i/**/] = data.get(i + 3); // A
                    imageBuffer.getData()[i + 1] = data.get(i + 2); // B
                    imageBuffer.getData()[i + 2] = data.get(i + 1); // G
                    imageBuffer.getData()[i + 3] = data.get(i/**/); // R
                }

                JOptionPane.showMessageDialog(null, new ImageIcon(image));
            }
        }

        renderPipeline.close();
        module.close();
        view.close();
        texture.close();
        buffer.close();
    }

    private RenderPipeline createRenderPipeline(ShaderModule module, TextureFormat format) {
        var vertex = VertexState.builder()
            .module(module)
            .entryPoint("vs_main")
            .build();

        var primitive = PrimitiveState.builder()
            .topology(PrimitiveTopology.TRIANGLE_LIST)
            .frontFace(FrontFace.CCW)
            .build();

        var multisample = MultisampleState.builder()
            .count(1)
            .mask(0xFFFFFFFF)
            .alphaToCoverageEnabled(false)
            .build();

        var fragment = FragmentState.builder()
            .module(module)
            .entryPoint("fs_main")
            .addTargets(ColorTargetState.builder()
                .format(format)
                .addWriteMask(ColorWrites.ALL)
                .build())
            .build();

        var renderPipelineDescriptor = RenderPipelineDescriptor.builder()
            .vertex(vertex)
            .primitive(primitive)
            .multisample(multisample)
            .fragment(fragment)
            .build();

        return device.createRenderPipeline(renderPipelineDescriptor);
    }

    @Override
    public void close() {
        device.close();
        adapter.close();
        instance.close();
    }

    static void main() throws IOException {
        System.loadLibrary("lib/wgpu_native");

        try (var main = new App()) {
            main.run();
        }
    }
}
