package de.nikschadowsky.baall.parsergen.generator.elements;

import java.util.ArrayList;
import java.util.function.BinaryOperator;

/**
 * File created on 27.02.2024
 */
public class JavaSourceElementList<ELEMENT_TYPE extends JavaSourceElement<?>> extends ArrayList<ELEMENT_TYPE> implements JavaSourceElement<JavaSourceElementList<ELEMENT_TYPE>> {

    private final String placeholderId;
    private final BinaryOperator<String> sourceCodeFunction;

    public JavaSourceElementList(String placeholderId, BinaryOperator<String> sourceCodeFunction) {
        this.placeholderId = placeholderId;
        this.sourceCodeFunction = sourceCodeFunction;
    }

    @Override
    public String getSourceCodeSnippet() {
        return stream().map(JavaSourceElement::getSourceCodeSnippet).reduce(
                "", sourceCodeFunction
        );
    }

    public String getPlaceholderId() {
        return placeholderId;
    }
}
