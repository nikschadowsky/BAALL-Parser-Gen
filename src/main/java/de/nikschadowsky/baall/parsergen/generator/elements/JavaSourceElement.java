package de.nikschadowsky.baall.parsergen.generator.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * File created on 27.02.2024
 */
public interface JavaSourceElement<ELEMENT_TYPE> {

    @NotNull String getSourceCodeSnippet();

    static String joinArgumentsString(JavaSourceElement<?>... elements) {
        List<String> elementCodeSnippets = Arrays.stream(elements)
                                                 .map(JavaSourceElement::getSourceCodeSnippet)
                                                 .filter(not(String::isBlank))
                                                 .collect(Collectors.toList());

        return String.join(" ", elementCodeSnippets);
    }

    @Unmodifiable Map<Class<?>, String> PRIMITIVE_CLASS_NAMES = Map.of(
            Byte.class,
            "byte",
            Short.class,
            "short",
            Integer.class,
            "int",
            Long.class,
            "long",
            Float.class,
            "float",
            Double.class,
            "double",
            Character.class,
            "char"
    );

    static String getSimpleTypeName(@NotNull Class<?> type){
        return PRIMITIVE_CLASS_NAMES.getOrDefault(type,  type.getSimpleName());
    }

}
