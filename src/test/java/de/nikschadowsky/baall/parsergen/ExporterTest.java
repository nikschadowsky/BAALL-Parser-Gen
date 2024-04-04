package de.nikschadowsky.baall.parsergen;

import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.generation.GrammarReader;
import de.nikschadowsky.baall.parsergen.util.FileUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * File created on 03.04.2024
 */
class ExporterTest {

    @TempDir
    private Path targetDestination;

    @Test
    void exportClasses() {
        Grammar grammar =
                GrammarReader.getInstance()
                             .generateGrammar(FileUtility.getPathFromClasspath(
                                     "exporter/ExporterTestGrammar.grammar"));

        Exporter.getInstance().exportClasses(grammar, targetDestination, "test.package", true);

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
    }

}