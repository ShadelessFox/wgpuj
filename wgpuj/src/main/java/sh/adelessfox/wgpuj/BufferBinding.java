package sh.adelessfox.wgpuj;

import java.util.OptionalLong;

public record BufferBinding(Buffer buffer, long offset, OptionalLong size) {
}
