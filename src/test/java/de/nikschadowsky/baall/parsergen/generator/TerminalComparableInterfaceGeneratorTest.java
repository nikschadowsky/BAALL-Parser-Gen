package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.lang.model.element.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * File created on 06.03.2024
 */
class TerminalComparableInterfaceGeneratorTest {

    @Test
    void testGenerate() {
        Grammar notNullMock = Mockito.mock(Grammar.class);

        TypeSpec interfaceSpec = TerminalComparableInterfaceGenerator.getInstance().generateTypeSpec(notNullMock);

        assertEquals(TypeSpec.Kind.INTERFACE, interfaceSpec.kind);
        assertEquals("TerminalComparable", interfaceSpec.name);
        assertTrue(interfaceSpec.hasModifier(Modifier.PUBLIC));

        assertEquals(2, interfaceSpec.methodSpecs.size());

        MethodSpec getTypeMethod = interfaceSpec.methodSpecs.get(0);
        assertEquals("getType", getTypeMethod.name);

        assertEquals(0, getTypeMethod.parameters.size());
        assertEquals(JavaSourceGenerator.TERMINAL_TYPE_ENUM_TYPENAME, getTypeMethod.returnType);

        MethodSpec getValueMethod = interfaceSpec.methodSpecs.get(1);
        assertEquals("getValue", getValueMethod.name);

        assertEquals(0, getValueMethod.parameters.size());
        assertEquals(ClassName.get(String.class), getValueMethod.returnType);

        assertEquals(ClassName.get(String.class), getValueMethod.returnType);
    }


}