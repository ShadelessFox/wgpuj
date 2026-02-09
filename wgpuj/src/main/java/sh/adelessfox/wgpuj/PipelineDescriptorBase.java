package sh.adelessfox.wgpuj;

import java.util.Optional;

public interface PipelineDescriptorBase extends ObjectDescriptorBase {
    Optional<PipelineLayout> layout();
}
