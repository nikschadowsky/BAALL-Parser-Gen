package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.lang.model.element.Modifier;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * File created on 01.04.2024
 */
class TerminalSymbolClassGeneratorTest {

    private final Grammar mock = Mockito.mock(Grammar.class);

    @Test
    void generateTypeSpec() {
        TypeSpec terminalClassTypeSpec = TerminalSymbolClassGenerator.getInstance().generateTypeSpec(mock);

        assertEquals(JavaSourceGenerator.TERMINAL_CLASS_TYPENAME.simpleName(), terminalClassTypeSpec.name);
        assertEquals(4, terminalClassTypeSpec.methodSpecs.size());
        assertTrue(terminalClassTypeSpec.hasModifier(Modifier.PRIVATE));
        assertTrue(terminalClassTypeSpec.hasModifier(Modifier.STATIC));

        for (String methodName : Set.of("<init>", "symbolMatches", "getType", "getValue")) {
            assertTrue(terminalClassTypeSpec.methodSpecs.stream().map(ms -> ms.name).anyMatch(methodName::equals));
        }

        // symbolMatches
        MethodSpec symbolMatchesMethodSpec = getMethodSpecFromName(terminalClassTypeSpec, "symbolMatches");
        assertEquals(TypeName.BOOLEAN, symbolMatchesMethodSpec.returnType);
        assertEquals(1, symbolMatchesMethodSpec.parameters.size());
        assertEquals(getExpectedSymbolMatchesCode(), symbolMatchesMethodSpec.code.toString().strip());

        // getType
        MethodSpec getTypeMethodSpec = getMethodSpecFromName(terminalClassTypeSpec, "getType");
        assertEquals(JavaSourceGenerator.TERMINAL_TYPE_ENUM_TYPENAME, getTypeMethodSpec.returnType);
        assertEquals(0, getTypeMethodSpec.parameters.size());
        assertEquals("return type;", getTypeMethodSpec.code.toString().strip());

        // getValue
        MethodSpec getValueMethodSpec = getMethodSpecFromName(terminalClassTypeSpec, "getValue");
        assertEquals(ClassName.get(String.class), getValueMethodSpec.returnType);
        assertEquals(0, getValueMethodSpec.parameters.size());
        assertEquals("return value;", getValueMethodSpec.code.toString().strip());
    }

    @NotNull
    private static MethodSpec getMethodSpecFromName(TypeSpec terminalClassTypeSpec, String getType) {
        return terminalClassTypeSpec.methodSpecs.stream()
                                                .filter(ms -> getType.equals(ms.name))
                                                .findAny()
                                                .orElseThrow();
    }

    private String getExpectedSymbolMatchesCode() {
        return """
                if (!TerminalType.ANY.equals(type)) {
                  return value.equals(symbol.getValue());
                } if (type.equals(symbol.getType())) {
                  return !type.hasExactValueMatching() || value.equals(symbol.getType());
                }
                return false;""";
    }
}