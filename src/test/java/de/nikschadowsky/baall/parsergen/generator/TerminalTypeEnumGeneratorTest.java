package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.lang.model.element.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * File created on 07.03.2024
 */
class TerminalTypeEnumGeneratorTest {

    @Test
    void generateTypeSpec() {
        Grammar notNullMock = Mockito.mock(Grammar.class);

        TypeSpec enumSpec = TerminalTypeEnumGenerator.getInstance().generateTypeSpec(notNullMock);

        assertEquals(TypeSpec.Kind.ENUM, enumSpec.kind);
        assertEquals("TerminalType", enumSpec.name);
        assertTrue(enumSpec.hasModifier(Modifier.PUBLIC));

        // fields
        assertEquals(2, enumSpec.fieldSpecs.size());
        assertTrue(enumSpec.fieldSpecs.stream()
                                      .anyMatch(f -> "description".equals(f.name)
                                              && ClassName.get(String.class).equals(f.type)));
        assertTrue(enumSpec.fieldSpecs.stream()
                                      .anyMatch(f -> "hasExactValueMatching".equals(f.name)
                                              && TypeName.BOOLEAN.equals(f.type)));

        Arrays.stream(GrammarTerminal.TerminalType.values()).forEach(type -> {
            assertTrue(enumSpec.enumConstants.containsKey(type.name()));
            assertEquals("\"%s\", %s".formatted(
                                 type.getDescription(),
                                 type.hasExactValueMatching()
                         ), enumSpec.enumConstants
                                 .get(type.name())
                                 .anonymousTypeArguments
                                 .toString()
            );
        });

        // methods: constructor, getDescription, hasExactValueMatching
        assertEquals(3, enumSpec.methodSpecs.size());
        assertEquals(1, enumSpec.methodSpecs.stream().filter(MethodSpec::isConstructor).count());
        assertTrue(enumSpec.methodSpecs.stream()
                                       .anyMatch((method -> "getDescription".equals(method.name)
                                               && method.returnType.equals(ClassName.get(String.class))
                                               && method.parameters.isEmpty())
                                       ));
        assertTrue(enumSpec.methodSpecs.stream()
                                       .anyMatch((method -> "hasExactValueMatching".equals(method.name)
                                               && method.returnType.equals(TypeName.BOOLEAN)
                                               && method.parameters.isEmpty())
                                       ));

    }
}