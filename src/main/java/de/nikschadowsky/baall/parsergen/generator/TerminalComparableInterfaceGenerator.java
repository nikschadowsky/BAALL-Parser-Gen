package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;

/**
 * File created on 26.02.2024
 */
public class TerminalComparableInterfaceGenerator implements JavaSourceGenerator {

    private static TerminalComparableInterfaceGenerator INSTANCE;

    public static TerminalComparableInterfaceGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TerminalComparableInterfaceGenerator();
        }

        return INSTANCE;
    }

    private TerminalComparableInterfaceGenerator() {
    }

    /**
     * @param grammar the {@link Grammar} object to generate this {@link TypeSpec} for
     */
    @Override
    public @NotNull TypeSpec generateTypeSpec(@NotNull Grammar grammar) {
        MethodSpec typeGetterMethod = MethodSpec.methodBuilder("getType")
                                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                                .returns(TERMINAL_TYPE_ENUM_TYPENAME)
                                                .build();

        MethodSpec valueGetterMethod = MethodSpec.methodBuilder("getValue")
                                                 .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                                 .returns(String.class)
                                                 .build();


        return TypeSpec.interfaceBuilder(TERMINAL_COMPARABLE_INTERFACE_TYPENAME)
                       .addModifiers(Modifier.PUBLIC)
                       .addMethod(typeGetterMethod)
                       .addMethod(valueGetterMethod)
                       .addAnnotation(AnnotationSpec.builder(Generated.class)
                                                    .addMember("value", "$S", "by BAALL-Parser-Gen")
                                                    .build())
                       .build();
    }
}
