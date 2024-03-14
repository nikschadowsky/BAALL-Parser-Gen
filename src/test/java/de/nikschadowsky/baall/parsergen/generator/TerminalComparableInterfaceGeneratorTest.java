package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
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

        assertEquals(1, interfaceSpec.methodSpecs.size());
        MethodSpec method = interfaceSpec.methodSpecs.get(0);
        assertEquals("symbolMatches", method.name);

        assertEquals(1, method.parameters.size());
        assertEquals("terminal", method.parameters.get(0).name);
        assertEquals("TerminalComparable", method.parameters.get(0).type.toString());

        assertEquals(TypeName.BOOLEAN, method.returnType);
    }


}