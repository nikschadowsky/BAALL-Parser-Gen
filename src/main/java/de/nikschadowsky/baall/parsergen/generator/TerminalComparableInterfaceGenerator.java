package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import org.jetbrains.annotations.NotNull;

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
        MethodSpec typeGetterMethod =
                MethodSpec.methodBuilder("getType")
                          .returns(TERMINAL_TYPE_INTERFACE_TYPENAME)
                          .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                          .build();

        MethodSpec valueGetterMethod =
                MethodSpec.methodBuilder("getValue")
                          .returns(ClassName.get(String.class))
                          .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                          .build();


        return TypeSpec.interfaceBuilder(TERMINAL_COMPARABLE_INTERFACE_TYPENAME)
                       .addModifiers(Modifier.PUBLIC)
                       .addMethod(typeGetterMethod)
                       .addMethod(valueGetterMethod)
                       .build();
    }
}
