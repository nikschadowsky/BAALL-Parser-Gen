package de.nikschadowsky.baall.parsergen.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtility {

    public static String getFileContent(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't get content from the specified file '%s'!".formatted(path), e);
        }
    }

    public static String getFileContentFromClasspath(String path) {
        return getFileContent(getPathFromClasspath(path));
    }

    public static String getFileContentFromFileSystem(String path) {
        return getFileContent(getPathFromFileSystem(path));
    }

    public static Path getPathFromClasspath(String path) {
        try {
            return Paths.get(ClassLoader.getSystemResource(path).toURI());
        } catch (InvalidPathException | URISyntaxException e) {
            throw new RuntimeException("Specified File '%s' does not exist in the class path!".formatted(path), e);
        }
    }

    public static Path getPathFromFileSystem(String path) {
        Path resolvedPath = Paths.get(path);

        if (!Files.exists(resolvedPath)) {
            throw new RuntimeException("Specified File '%s' does not exist in the class path!".formatted(path));
        }
        return resolvedPath;
    }

    public static String getFileExtension(@NotNull Path path) {
        String filename = path.getFileName().toString();
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static boolean createFile(Path filePath) {
        try {
            Files.createFile(filePath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void writeToFile(Path path, String data, boolean overrideFile) throws IOException {
        List<StandardOpenOption> options = new ArrayList<>();
        options.add(StandardOpenOption.WRITE);
        options.add(StandardOpenOption.CREATE);

        if (Files.exists(path)) {
            if (!overrideFile) {
                return;
            }
            // delete existing contents
            options.add(StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            createFile(path);
        }

        Files.writeString(path, data, options.toArray(new OpenOption[0]));
    }
}
