package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.objects.PipelineLayout;

import java.util.Optional;

public interface PipelineDescriptorBase extends ObjectDescriptorBase {
    Optional<PipelineLayout> layout();
}
