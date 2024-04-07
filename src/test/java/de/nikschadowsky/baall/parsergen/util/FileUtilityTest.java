package de.nikschadowsky.baall.parsergen.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * File created on 26.02.2024
 */
class FileUtilityTest {

    @Test
    void getFileContentFromClasspath() {
        String expected = "this is text";

        assertEquals(expected, FileUtility.getFileContentFromClasspath("utility/testFileContentFromClasspath.txt"));
    }

    @Test
    void getFileContentFromFileSystem() throws IOException {
        Path filepath = Path.of(String.format("../testFile%s.tmp", System.currentTimeMillis()));
        String expectedFileContent = "this is text";

        Files.createFile(filepath);
        Files.writeString(filepath, expectedFileContent);

        assertEquals(expectedFileContent, FileUtility.getFileContentFromFileSystem(filepath.toString()));

        Files.deleteIfExists(filepath);
    }

    @Test
    void getFileExtension() {
        String path = "a/b/c/d/e/f.g";

        assertEquals("g", FileUtility.getFileExtension(Path.of(path)));
    }

    @Test
    void createFile() throws IOException {
        Path filepath = Path.of("..", "myfile.tmp");

        assertTrue(FileUtility.createFile(filepath));
        assertTrue(Files.exists(filepath));

        Files.deleteIfExists(filepath);
    }

    @TempDir
    private Path tempDir;

    @Test
    void writeToFileExistingFile() throws IOException {
        //setup
        Path target = tempDir.resolve("writeToFile_fileExists.test");
        Files.createFile(target);
        Files.writeString(target, "test12345");

        FileUtility.writeToFile(target, "content", true);

        assertEquals("content", FileUtility.getFileContent(target));

        //Files.deleteIfExists(target);
    }

    @Test
    void writeToFileNotExistingFile() throws IOException {
        Path target = tempDir.resolve("writeToFile_fileDoesNotExist.test");

        FileUtility.writeToFile(target, "content", true);

        assertEquals("content", FileUtility.getFileContent(target));
    }
}