package de.nikschadowsky.baall.parsergen.generator.elements;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File created on 27.02.2024
 */
public interface JavaSourceElement<ELEMENT_TYPE> {

    String getSourceCodeSnippet();


    static String joinArgumentsString(JavaSourceElement<?>... elements) {
        List<String> elementCodeSnippets = Arrays.stream(elements)
                                                 .map(JavaSourceElement::getSourceCodeSnippet)
                                                 .collect(Collectors.toList());

        return String.join(" ", elementCodeSnippets);
    }

}
