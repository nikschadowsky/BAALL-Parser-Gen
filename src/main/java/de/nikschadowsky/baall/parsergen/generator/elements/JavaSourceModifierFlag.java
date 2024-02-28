package de.nikschadowsky.baall.parsergen.generator.elements;

/**
 * File created on 27.02.2024
 */
public enum JavaSourceModifierFlag implements JavaSourceElement<JavaSourceModifierFlag> {
    FINAL("final"), NONE(""), STATIC("static");

    private final String sourceCodeSnippet;

    JavaSourceModifierFlag(String sourceCodeSnippet) {
        this.sourceCodeSnippet = sourceCodeSnippet;
    }

    @Override
    public String getSourceCodeSnippet() {
        return sourceCodeSnippet;
    }
}
