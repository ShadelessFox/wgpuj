package sh.adelessfox.wgpuj;

import sh.adelessfox.wgpuj.objects.ShaderModule;

import java.util.List;
import java.util.Optional;

public interface ProgrammableStage {
    ShaderModule module();

    Optional<String> entryPoint();

    List<ConstantEntry> constants();
}
