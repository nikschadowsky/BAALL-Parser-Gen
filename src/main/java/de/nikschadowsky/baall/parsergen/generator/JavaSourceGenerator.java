package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 26.02.2024
 */
public interface JavaSourceGenerator {

     ClassName TERMINAL_TYPE_INTERFACE_TYPENAME = ClassName.bestGuess("TerminalType");
     ClassName TERMINAL_COMPARABLE_INTERFACE_TYPENAME = ClassName.bestGuess("TerminalComparable");
     ClassName PARSER_CLASS_TYPENAME = ClassName.bestGuess("Parser");
     ClassName PARSER_INNER_TERMINAL_TYPENAME = ClassName.bestGuess("TerminalSymbol");

    @NotNull TypeSpec generateTypeSpec(@NotNull Grammar grammar);

}
