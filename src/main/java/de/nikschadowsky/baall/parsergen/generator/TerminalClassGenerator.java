package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.*;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;

/**
 * File created on 27.03.2024
 */
public class TerminalClassGenerator implements JavaSourceGenerator {

    private static TerminalClassGenerator INSTANCE;

    public static TerminalClassGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TerminalClassGenerator();
        }

        return INSTANCE;
    }

    private TerminalClassGenerator() {
    }

    @Override
    public @NotNull TypeSpec generateTypeSpec(@NotNull Grammar grammar) {
        FieldSpec valueFieldSpec = FieldSpec.builder(String.class, "value", Modifier.PRIVATE, Modifier.FINAL).build();

        FieldSpec typeFieldSpec =
                FieldSpec.builder(TERMINAL_TYPE_ENUM_TYPENAME, "type", Modifier.PRIVATE, Modifier.FINAL).build();

        MethodSpec symbolMatchesMethodSpec =
                MethodSpec.methodBuilder("symbolMatches")
                          .addModifiers(Modifier.PUBLIC)
                          .returns(TypeName.BOOLEAN)
                          .addParameter(
                                  JavaSourceGenerator.TERMINAL_COMPARABLE_INTERFACE_TYPENAME,
                                  "symbol"
                          ).addCode(
                                  CodeBlock.builder()
                                           .beginControlFlow(
                                                   "if (!$T.ANY.equals(type))",
                                                   JavaSourceGenerator.TERMINAL_TYPE_ENUM_TYPENAME
                                           )
                                           .addStatement(
                                                   "return value.equals(symbol.getValue())")
                                           .nextControlFlow(
                                                   "if (type.equals(symbol.getType())")
                                           .addStatement(
                                                   "return !type.hasExactValueMatching() || value.equals(symbol.getType())")
                                           .endControlFlow()
                                           .addStatement("return false")
                                           .build()
                          ).build();

        MethodSpec getTypeMethodSpec = MethodSpec.methodBuilder("getType")
                                                 .addAnnotation(Override.class)
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(TERMINAL_TYPE_ENUM_TYPENAME)
                                                 .addStatement("return type")
                                                 .build();

        MethodSpec getValueMethodSpec = MethodSpec.methodBuilder("getValue")
                                                  .addAnnotation(Override.class)
                                                  .addModifiers(Modifier.PUBLIC)
                                                  .returns(String.class)
                                                  .addStatement("return value")
                                                  .build();


        return TypeSpec.classBuilder(TERMINAL_CLASS_TYPENAME)
                       .addModifiers(Modifier.PUBLIC)
                       .addSuperinterface(TERMINAL_COMPARABLE_INTERFACE_TYPENAME)
                       .addField(valueFieldSpec)
                       .addField(typeFieldSpec)
                       .addMethod(symbolMatchesMethodSpec)
                       .addMethod(getTypeMethodSpec)
                       .addMethod(getValueMethodSpec)
                       .build();
    }
}
