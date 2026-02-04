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
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Mojo(name = "prepare-native-module", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class PrepareNativeModuleMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(name = "upstream", defaultValue = "gfx-rs/wgpu-native", required = true)
    private String upstream;

    @Parameter(name = "version", required = true)
    private String version;

    @Parameter(name = "classifier", required = true)
    private String classifier;

    @Parameter(name = "outputName", required = true)
    private String outputName;

    @Parameter(name = "outputDirectory", required = true)
    private Path outputDirectory;

    @Override
    public void execute() throws MojoFailureException {
        var root = Path.of(project.getBuild().getDirectory());
        var natives = loadNatives(root);

        var modulePath = outputDirectory;
        var nativesPath = modulePath.resolve("sh/adelessfox/wgpuj/natives/windows/amd64");

        try (FileSystem fs = FileSystems.newFileSystem(natives)) {
            Files.createDirectories(nativesPath);
            Files.copy(fs.getPath("lib/wgpu_native.dll"), nativesPath.resolve("wgpu_native.dll"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new MojoFailureException("Failed to create output directories", e);
        }

        var compiler = ToolProvider.getSystemJavaCompiler();
        var diagnosticCollector = new DiagnosticCollector<>();
        var fileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.getDefault(), StandardCharsets.UTF_8);

        var units = new ArrayList<JavaFileObject>();
        units.add(new MemoryJavaFileObject(
            "module-info.java",
            """
                module sh.adelessfox.wgpuj.natives {
                    opens sh.adelessfox.wgpuj.natives.windows.amd64;
                }
                """
        ));

        var task = compiler.getTask(
            null,
            fileManager,
            diagnosticCollector,
            List.of("-d", modulePath.toAbsolutePath().toString()),
            null,
            units
        );

        boolean result = task.call();
        if (!result) {
            throw new MojoFailureException("Failed to compile natives module");
        }
    }

    private Path loadNatives(Path buildDirectory) throws MojoFailureException {
        try {
            var asset = fetchNatives();
            var name = asset.get("name").getAsString();
            var digest = asset.get("digest").getAsString();
            var url = URI.create(asset.get("browser_download_url").getAsString());
            return downloadNatives(name, digest, url, buildDirectory);
        } catch (Exception e) {
            throw new MojoFailureException("Failed to download natives", e);
        }
    }

    private JsonObject fetchNatives() throws IOException {
        JsonObject release;
        try (HttpClient client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/%s/releases/tags/%s".formatted(upstream, version)))
                .build();
            var handler = HttpResponse.BodySubscribers.mapping(
                HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8),
                body -> JsonParser.parseString(body).getAsJsonObject()
            );
            var response = client.send(request, info -> handler);
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
            release = response.body();
        } catch (Exception e) {
            throw new IOException("Failed to fetch releases", e);
        }

        var assets = release.getAsJsonArray("assets");
        var asset = IntStream.range(0, assets.size())
            .mapToObj(i -> assets.get(i).getAsJsonObject())
            .filter(a -> a.get("name").getAsString().equals(classifier + ".zip"))
            .findFirst().orElse(null);

        if (asset == null) {
            throw new IOException("Can't find asset with classifier " + classifier);
        }

        return asset;
    }

    private Path downloadNatives(String name, String digest, URI uri, Path buildDirectory) throws IOException, InterruptedException {
        var cache = buildDirectory.resolve("wgpu-natives-cache");
        Files.createDirectories(cache);

        var outputFile = cache.resolve(name);
        var digestFile = cache.resolve(name + ".digest");

        if (Files.exists(digestFile)) {
            var localDigest = Files.readString(digestFile);
            if (localDigest.equals(digest)) {
                getLog().info("Natives are up to date, skipping download");
                return outputFile;
            }
        }

        try (
            var client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build()
        ) {
            var response = client.send(
                HttpRequest.newBuilder(uri).build(),
                HttpResponse.BodyHandlers.ofFile(outputFile)
            );
            if (response.statusCode() != 200) {
                throw new IOException("Unexpected status code: " + response.statusCode());
            }
        }

        Files.writeString(digestFile, digest);
        getLog().info("Natives downloaded to " + outputFile);

        return outputFile;
    }

    private static void compile() {
        var compiler = ToolProvider.getSystemJavaCompiler();
        var diagnosticCollector = new DiagnosticCollector<>();
        var fileManager = compiler.getStandardFileManager(diagnosticCollector, Locale.getDefault(), StandardCharsets.UTF_8);

        var units = new ArrayList<JavaFileObject>();
        units.add(new MemoryJavaFileObject(
            "module-info.java",
            """
                module sh.adelessfox.wgpuj.natives {
                    opens sh.adelessfox.wgpuj.natives.windows.amd64;
                }
                """
        ));

        var task = compiler.getTask(
            null,
            fileManager,
            diagnosticCollector,
            List.of("-d", "out"),
            null,
            units
        );

        System.out.println(task.call());
    }

    private static void downloadNatives(URI uri, Path dst) throws IOException {
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
