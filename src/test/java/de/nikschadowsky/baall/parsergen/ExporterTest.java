package de.nikschadowsky.baall.parsergen;

import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.generation.GrammarReader;
import de.nikschadowsky.baall.parsergen.util.FileUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File created on 03.04.2024
 */
class ExporterTest {

    @TempDir
    private Path targetDestination;

    @Test
    void exportClasses() throws IOException {
        Grammar grammar =
                GrammarReader.getInstance()
                             .generateGrammar(FileUtility.getPathFromClasspath(
                                     "exporter/ExporterTestGrammar.grammar"));

        Exporter.getInstance().exportClasses(grammar, targetDestination, "generator_output", true);

        Path generatorOutput = targetDestination.resolve("generator_output");

        try (Stream<Path> stream = Files.list(generatorOutput)) {
            assertEquals(3, stream.count());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Stream<Path> stream = Files.list(generatorOutput)) {
            assertTrue(stream.allMatch(p -> "java".equals(FileUtility.getFileExtension(p))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileManager fileManager = new NoClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));

        Iterable<? extends JavaFileObject> compilationUnits = getCompilationUnits(generatorOutput);

        JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

        if (!task.call()) {
            diagnostics.getDiagnostics().forEach(System.err::println);
            fail("Compilation failed");
        }
        fileManager.close();
    }

    private Iterable<? extends JavaFileObject> getCompilationUnits(Path dir) {
        File[] files = dir.toFile().listFiles();

        List<SimpleJavaFileObject> compilationUnits = new ArrayList<>();

        assert files != null;
        for (File file : files) {
            compilationUnits.add(new TestJavaFileObject(file.toPath()));
        }
        return compilationUnits;
    }


    private static class TestJavaFileObject extends SimpleJavaFileObject {

        private final Path path;

        protected TestJavaFileObject(Path path) {
            super(path.toUri(), Kind.SOURCE);
            this.path = path;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return FileUtility.getFileContent(path);
        }
    }

    private static class NoClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        /**
         * Creates a new instance of {@code ForwardingJavaFileManager}.
         *
         * @param fileManager delegate to this file manager
         */
        protected NoClassFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            return new SimpleJavaFileObject(
                    URI.create("string:///" + className.replace('.', '/') + kind.extension),
                    kind
            ) {
                @Override
                public OutputStream openOutputStream() {
                    // Suppress output by returning a dummy output stream
                    return new ByteArrayOutputStream();
                }
            };
        }
    }
}