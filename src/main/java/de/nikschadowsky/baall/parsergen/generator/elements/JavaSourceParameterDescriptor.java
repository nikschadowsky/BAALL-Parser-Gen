package de.nikschadowsky.baall.parsergen.generator.elements;

/**
 * File created on 28.02.2024
 */
public class JavaSourceParameterDescriptor<FIELD_TYPE> extends JavaSourceNamespaceDescriptor<FIELD_TYPE> {

    public JavaSourceParameterDescriptor(Class<FIELD_TYPE> type, String name) {
        super(JavaSourceAccessLevel.NONE, JavaSourceModifierFlag.NONE, type, name);
    }

    @Override
    public String getSourceCodeSnippet() {
        return "%s %s".formatted();
    }
}
