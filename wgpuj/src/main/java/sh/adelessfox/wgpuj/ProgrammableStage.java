package sh.adelessfox.wgpuj;

import java.util.List;
import java.util.Optional;

public interface ProgrammableStage {
    ShaderModule module();

    Optional<String> entryPoint();

    List<ConstantEntry> constants();
}
