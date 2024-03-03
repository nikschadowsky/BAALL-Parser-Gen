package de.nikschadowsky.baall.parsergen.generator.elements.definition;

import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElement;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 27.02.2024
 */
public enum JavaSourceModifierFlag implements JavaSourceElement<JavaSourceModifierFlag> {
    FINAL("final"),
    NONE(""),
    STATIC("static"),
    STATIC_FINAL("static final");

    private final String sourceCodeSnippet;

    JavaSourceModifierFlag(String sourceCodeSnippet) {
        this.sourceCodeSnippet = sourceCodeSnippet;
    }

    @Override
    public @NotNull String getSourceCodeSnippet() {
        return sourceCodeSnippet;
    }
}
