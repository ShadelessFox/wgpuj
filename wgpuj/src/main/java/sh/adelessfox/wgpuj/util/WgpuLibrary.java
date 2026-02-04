package sh.adelessfox.wgpuj.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

/**
 * A helper class for loading OS-specific native libraries from a jar file.
 */
public final class WgpuLibrary {
    /** System property to specify a custom path to the native library. */
    private static final String WGPUJ_LIBRARY_PATH = "wgpuj.library.path";
    private static final String WGPUJ_LIBRARY_NAME = "wgpu_native";

    private WgpuLibrary() {
    }

    public static synchronized SymbolLookup load() {
        var libraryPath = System.getProperty(WGPUJ_LIBRARY_PATH);
        if (libraryPath != null) {
            return SymbolLookup.libraryLookup(Path.of(libraryPath), Arena.ofShared());
        }
        try (InputStream in = openLibrary()) {
            if (in == null) {
                throw new UnsupportedOperationException(
                    """
                        Couldn't find native library for wgpuj.
                        1. Ensure you are using the correct native library for your OS and architecture
                        2. Ensure your module-info has `requires sh.adelessfox.wgpuj.natives`
                        3. Alternatively, set the system property 'wgpuj.library.path' to the path of the native library
                        """);
            }
            var path = Files.createTempFile("wgpuj", ".dll");
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            return SymbolLookup.libraryLookup(path, Arena.ofShared());
        } catch (IOException e) {
            throw new UncheckedIOException("Couldn't load native library for wgpuj", e);
        }
    }

    private static InputStream openLibrary() throws IOException {
        var thisModule = WgpuLibrary.class.getModule();
        var moduleLayer = thisModule.getLayer();
        if (moduleLayer == null) {
            return null;
        }
        var nativesModule = moduleLayer.findModule(thisModule.getName() + ".natives").orElse(null);
        if (nativesModule == null) {
            return null;
        }
        return nativesModule.getResourceAsStream(getLibraryPath());
    }

    private static String getLibraryPath() {
        return "/sh/adelessfox/wgpuj/natives/"
               + getPlatformName() + "/"
               + getPlatformArch() + "/"
               + getPlatformLibrary();
    }

    private static String getPlatformName() {
        var name = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (name.startsWith("windows")) {
            return "windows";
        } else if (name.startsWith("linux")) {
            return "linux";
        } else if (name.startsWith("mac")) {
            return "macos";
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + name);
        }
    }

    private static String getPlatformArch() {
        var arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        if (arch.equals("x86_64") || arch.equals("amd64")) {
            return "amd64";
        } else if (arch.equals("aarch64") || arch.equals("arm64")) {
            return "arm64";
        } else {
            throw new UnsupportedOperationException("Unsupported architecture: " + arch);
        }
    }

    private static String getPlatformLibrary() {
        return System.mapLibraryName(WGPUJ_LIBRARY_NAME);
    }
}
