package sh.adelessfox.wgpuj.test;

import sh.adelessfox.wgpuj.*;
import sh.adelessfox.wgpuj.objects.Device;
import sh.adelessfox.wgpuj.objects.Instance;
import sh.adelessfox.wgpuj.objects.RenderPipeline;
import sh.adelessfox.wgpuj.objects.ShaderModule;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Demo {
    private static final String SHADER = """
        @vertex
        fn vs_main(@builtin(vertex_index) in_vertex_index: u32) -> @builtin(position) vec4<f32> {
            let x = f32(i32(in_vertex_index) - 1);
            let y = f32(i32(in_vertex_index & 1u) * 2 - 1);
            return vec4<f32>(x, y, 0.0, 1.0);
        }
        
        @fragment
        fn fs_main() -> @location(0) vec4<f32> {
            return vec4<f32>(0.2, 0.6, 0.4, 1.0);
        }
        """;

    static void main() {
        var instance = Instance.create(ImmutableInstanceDescriptor.builder()
            .addFlags(InstanceFlag.DEBUG, InstanceFlag.VALIDATION)
            .build());

        var adapter = instance.requestAdapter();

        var device = adapter.requestDevice(ImmutableDeviceDescriptor.builder()
            .label("default device")
            .build());

        int width = 1024;
        int height = 512;
        var format = TextureFormat.RGBA8_UNORM;

        var buffer = device.createBuffer(ImmutableBufferDescriptor.builder()
            .size(width * height * 4)
            .addUsages(BufferUsage.COPY_DST, BufferUsage.MAP_READ)
            .mappedAtCreation(false)
            .build());

        var texture = device.createTexture(ImmutableTextureDescriptor.builder()
            .size(ImmutableExtent3D.builder()
                .width(width)
                .height(height)
                .build())
            .format(format)
            .addUsages(TextureUsage.COPY_SRC, TextureUsage.RENDER_ATTACHMENT)
            .build());

        var view = texture.createView();

        var module = device.createShaderModule(ImmutableShaderModuleDescriptor.builder()
            .label("shader")
            .source(ImmutableShaderSource.Wgsl.of(SHADER))
            .build());

        var renderPipeline = createRenderPipeline(device, module, format);

        try (var encoder = device.createCommandEncoder(ImmutableCommandEncoderDescriptor.of())) {
            var descriptor = ImmutableRenderPassDescriptor.builder()
                .addColorAttachments(ImmutableRenderPassColorAttachment.builder()
                    .view(view)
                    .ops(ImmutableOperations.of(
                        ImmutableOperations.Clear.of(ImmutableColor.of(0.2, 0.1, 0.7, 1.0)),
                        Operations.StoreOp.STORE))
                    .build())
                .build();

            try (var pass = encoder.beginRenderPass(descriptor)) {
                pass.setPipeline(renderPipeline);
                pass.draw(3, 1, 0, 0);
                pass.end();
            }

            encoder.copyTextureToBuffer(
                ImmutableTexelCopyTextureInfo.of(texture),
                ImmutableTexelCopyBufferInfo.builder()
                    .layout(ImmutableTexelCopyBufferLayout.builder()
                        .bytesPerRow(width * 4)
                        .rowsPerImage(height)
                        .build())
                    .buffer(buffer)
                    .build(),
                ImmutableExtent3D.builder()
                    .width(width)
                    .height(height)
                    .build()
            );

            try (var encoded = encoder.finish()) {
                try (var queue = device.getQueue()) {
                    queue.submit(encoded);
                }
            }
        }

        try (var mapped = buffer.map(0, width * height * 4, MapMode.READ)) {
            var image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            var imageBuffer = (DataBufferByte) image.getRaster().getDataBuffer();

            var data = mapped.asBuffer(0, width * height * 4);
            for (int i = 0; i < width * height * 4; i += 4) {
                imageBuffer.getData()[i/**/] = data.get(i + 3); // A
                imageBuffer.getData()[i + 1] = data.get(i + 2); // B
                imageBuffer.getData()[i + 2] = data.get(i + 1); // G
                imageBuffer.getData()[i + 3] = data.get(i/**/); // R
            }

            JOptionPane.showMessageDialog(null, new ImageIcon(image));
        }

        renderPipeline.close();
        module.close();

        view.close();
        texture.close();

        buffer.close();

        device.close();
        adapter.close();
        instance.close();
    }

    private static RenderPipeline createRenderPipeline(Device device, ShaderModule module, TextureFormat format) {
        var vertex = ImmutableVertexState.builder()
            .module(module)
            .entryPoint("vs_main")
            .build();

        var primitive = ImmutablePrimitiveState.builder()
            .topology(PrimitiveTopology.TRIANGLE_LIST)
            .frontFace(FrontFace.CCW)
            .build();

        var multisample = ImmutableMultisampleState.builder()
            .count(1)
            .mask(0xFFFFFFFF)
            .alphaToCoverageEnabled(false)
            .build();

        var fragment = ImmutableFragmentState.builder()
            .module(module)
            .entryPoint("fs_main")
            .addTargets(ImmutableColorTargetState.of(format))
            .build();

        var renderPipelineDescriptor = ImmutableRenderPipelineDescriptor.builder()
            .vertex(vertex)
            .primitive(primitive)
            .multisample(multisample)
            .fragment(fragment)
            .build();

        return device.createRenderPipeline(renderPipelineDescriptor);
    }
}
