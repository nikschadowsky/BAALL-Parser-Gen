package de.nikschadowsky.baall.parsergen.generator.elements;

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
    public String getSourceCodeSnippet() {
        return sourceCodeRepresentation;
    }
}
