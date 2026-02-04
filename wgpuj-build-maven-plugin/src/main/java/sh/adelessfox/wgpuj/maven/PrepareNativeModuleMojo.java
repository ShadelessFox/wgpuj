package sh.adelessfox.wgpuj.maven;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.tools.DiagnosticCollector;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Mojo(name = "prepare-native-module", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class PrepareNativeModuleMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(name = "upstream", defaultValue = "gfx-rs/wgpu-native", required = true)
    private String upstream;

    @Parameter(name = "tag", required = true)
    private String tag;

    @Parameter(name = "os", required = true)
    private String os;

    @Parameter(name = "arch", required = true)
    private String arch;

    @Parameter(name = "outputDirectory", required = true)
    private Path outputDirectory;

    @Override
    public void execute() throws MojoFailureException {
        var platform = mapPlatform(os, arch);

        var modulePath = outputDirectory;
        var packageName = resolvePackageName(platform);
        var libraryName = resolveLibraryName(platform);

        // Copy the native library under the package name
        try (FileSystem fs = FileSystems.newFileSystem(retrieveNatives(platform))) {
            var target = modulePath.resolve(packageName.replace('.', '/'));
            Files.createDirectories(target);
            Files.copy(
                fs.getPath("lib", libraryName),
                target.resolve(libraryName),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to create output directories", e);
        }

        // Compile the module-info and place it in the module path
        compileModuleInfo(resolveModuleName(), packageName, modulePath);
    }

    /**
     * Compiles a module-info.java file with the given package name
     * and outputs it to the specified module path.
     *
     * @param moduleName  the name of the module
     * @param packageName the package name to open in the module
     * @param output      the path to output the compiled {@code module-info.class}
     * @throws MojoFailureException if an error occurs during compilation
     */
    private static void compileModuleInfo(
        String moduleName,
        String packageName,
        Path output
    ) throws MojoFailureException {
        var compiler = ToolProvider.getSystemJavaCompiler();
        var diagnostics = new DiagnosticCollector<>();
        var fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), StandardCharsets.UTF_8);

        var units = List.of(
            new MemoryJavaFileObject(
                "module-info.java",
                "module %s {\n\topens %s;\n}\n".formatted(moduleName, packageName)
            )
        );
        var options = List.of(
            "-d", output.toAbsolutePath().toString()
        );

        var task = compiler.getTask(null, fileManager, diagnostics, options, null, units);
        var success = task.call();

        if (!success) {
            throw new MojoFailureException("Failed to compile module-info.java: " + diagnostics.getDiagnostics());
        }
    }

    /**
     * Retrieves the native library for the given platform.
     *
     * @param platform the platform to retrieve the natives for
     * @return the path to the retrieved zip file containing the native library
     * @throws MojoFailureException if an error occurs while retrieving the natives
     */
    private Path retrieveNatives(Platform platform) throws MojoFailureException {
        var root = Path.of(project.getBuild().getDirectory());
        var cache = root.resolve("wgpu-natives-cache");

        try {
            var asset = fetchNativesAsset(platform);
            var name = asset.get("name").getAsString();
            var digest = asset.get("digest").getAsString();
            var url = URI.create(asset.get("browser_download_url").getAsString());
            return downloadAndCacheNatives(name, digest, url, cache);
        } catch (Exception e) {
            throw new MojoFailureException("Failed to download natives", e);
        }
    }

    /**
     * Fetches the release information from GitHub for the
     * given tag and finds the asset matching the given platform.
     *
     * @param platform the platform to find the asset for
     * @return the JSON object representing the asset
     * @throws IOException if an I/O error occurs or if the asset cannot be found
     */
    private JsonObject fetchNativesAsset(Platform platform) throws IOException {
        JsonObject release;
        try (HttpClient client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/%s/releases/tags/%s".formatted(upstream, tag)))
                .build();
            var handler = HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> JsonParser.parseString(body).getAsJsonObject());
            var response = client.send(request, info -> handler);
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
            release = response.body();
        } catch (Exception e) {
            throw new IOException("Failed to fetch releases", e);
        }

        var name = resolveArtifactName(platform);
        var assets = release.getAsJsonArray("assets");
        var asset = IntStream.range(0, assets.size())
            .mapToObj(i -> assets.get(i).getAsJsonObject())
            .filter(a -> a.get("name").getAsString().equals(name))
            .findFirst().orElse(null);

        if (asset == null) {
            throw new IOException("Can't find asset '" + name + "'");
        }

        return asset;
    }

    /**
     * Downloads a zip file containing the native library from the
     * given URL and caches it in the specified directory.
     * <p>
     * If a cached version exists and its digest matches
     * the provided digest, the cached version is used instead of downloading.
     *
     * @param name   the name of the artifact
     * @param digest the digest of the artifact, used to check if the cached version is up to date
     * @param uri    the URL to download the artifact from
     * @param output the directory to cache the artifact in
     * @return the path to the downloaded artifact
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    private Path downloadAndCacheNatives(
        String name,
        String digest,
        URI uri,
        Path output
    ) throws IOException, InterruptedException {
        var outputFile = output.resolve(name);
        var digestFile = output.resolve(name + ".digest");

        if (Files.exists(digestFile)) {
            var localDigest = Files.readString(digestFile);
            if (localDigest.equals(digest)) {
                getLog().info("Natives are up to date, skipping download");
                return outputFile;
            }
        }

        Files.createDirectories(output);

        try (
            var client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build()
        ) {
            var response = client.send(
                HttpRequest.newBuilder(uri).build(),
                HttpResponse.BodyHandlers.ofFile(outputFile));
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
        }

        Files.writeString(digestFile, digest);
        getLog().info("Natives downloaded to " + outputFile);

        return outputFile;
    }

    private static String resolveArtifactName(Platform platform) {
        var os = switch (platform.os()) {
            case WINDOWS -> "windows";
            case LINUX -> "linux";
            case MACOS -> "macos";
        };
        var arch = switch (platform.arch()) {
            case AMD64 -> "x86_64";
            case ARM64 -> "aarch64";
        };
        if (platform.os() == Platform.OperatingSystem.WINDOWS) {
            return "wgpu-" + os + "-" + arch + "-msvc-release.zip";
        } else {
            return "wgpu-" + os + "-" + arch + "-release.zip";
        }
    }

    private static String resolveModuleName() {
        return "sh.adelessfox.wgpuj.natives";
    }

    private static String resolvePackageName(Platform platform) {
        var os = switch (platform.os()) {
            case WINDOWS -> "windows";
            case LINUX -> "linux";
            case MACOS -> "macos";
        };
        var arch = switch (platform.arch()) {
            case AMD64 -> "amd64";
            case ARM64 -> "arm64";
        };
        return "sh.adelessfox.wgpuj.natives." + os + "." + arch;
    }

    private static String resolveLibraryName(Platform platform) {
        return switch (platform.os()) {
            case WINDOWS -> "wgpu_native.dll";
            case LINUX -> "libwgpu_native.so";
            case MACOS -> "libwgpu_native.dylib";
        };
    }

    private static Platform mapPlatform(String os, String arch) {
        var os1 = switch (os) {
            case "windows" -> Platform.OperatingSystem.WINDOWS;
            case "linux" -> Platform.OperatingSystem.LINUX;
            case "macos" -> Platform.OperatingSystem.MACOS;
            default -> throw new IllegalArgumentException("'os' must be one of 'windows', 'linux', 'macos', got " + os);
        };
        var arch1 = switch (arch) {
            case "amd64" -> Platform.Architecture.AMD64;
            case "arm64" -> Platform.Architecture.ARM64;
            default -> throw new IllegalArgumentException("'arch' must be one of 'amd64', 'arm64', got " + arch);
        };
        return new Platform(os1, arch1);
    }

    private record Platform(OperatingSystem os, Architecture arch) {
        private enum OperatingSystem {
            WINDOWS,
            LINUX,
            MACOS
        }

        private enum Architecture {
            AMD64,
            ARM64
        }
    }

    private static final class MemoryJavaFileObject extends SimpleJavaFileObject {
        private final String code;

        MemoryJavaFileObject(String name, String code) {
            super(Path.of(name).toUri(), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
