package de.nikschadowsky.baall.parsergen.generator.elements.definition;

import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElement;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 26.02.2024
 */
public enum JavaSourceAccessLevel implements JavaSourceElement<JavaSourceAccessLevel> {

    NONE(""),
    PRIVATE("private"),
    PROTECTED("protected"),
    PACKAGE_PROTECTED(""),
    PUBLIC("public");

    private final String sourceCodeRepresentation;

    JavaSourceAccessLevel(String sourceCodeRepresentation) {
        this.sourceCodeRepresentation = sourceCodeRepresentation;
    }

    @Override
    public @NotNull String getSourceCodeSnippet() {
        return sourceCodeRepresentation;
    }
}
