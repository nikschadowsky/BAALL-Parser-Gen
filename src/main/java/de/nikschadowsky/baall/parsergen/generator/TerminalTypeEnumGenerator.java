package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.*;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * File created on 07.03.2024
 */
public class TerminalTypeEnumGenerator implements JavaSourceGenerator {

    private static TerminalTypeEnumGenerator INSTANCE;

    public static TerminalTypeEnumGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TerminalTypeEnumGenerator();
        }

        return INSTANCE;
    }

    private TerminalTypeEnumGenerator() {
    }


    @Override
    public @NotNull TypeSpec generateTypeSpec(@NotNull Grammar grammar) {
        TypeSpec.Builder terminalTypeInterfaceTypeSpec =
                TypeSpec.enumBuilder(TERMINAL_TYPE_ENUM_TYPENAME).addModifiers(Modifier.PUBLIC);

        AnnotationSpec generatedAnnotationSpec =
                AnnotationSpec.builder(Generated.class).addMember("value", "$S", "by BAALL-Parser-Gen").build();
        terminalTypeInterfaceTypeSpec.addAnnotation(generatedAnnotationSpec);

        List<GrammarTerminal.TerminalType> entries = List.of(GrammarTerminal.TerminalType.values());

        entries.forEach(entry -> terminalTypeInterfaceTypeSpec.addEnumConstant(
                entry.name(),
                generateEnumEntryParameter(
                        entry.getDescription(),
                        entry.hasExactValueMatching()
                )
        ));

        terminalTypeInterfaceTypeSpec.addField(
                ClassName.get(String.class),
                "description",
                Modifier.PRIVATE,
                Modifier.FINAL
        );
        terminalTypeInterfaceTypeSpec.addField(
                TypeName.BOOLEAN,
                "hasExactValueMatching",
                Modifier.PRIVATE,
                Modifier.FINAL
        );

        MethodSpec constructorMethodSpec =
                MethodSpec.constructorBuilder()
                          .addParameter(ClassName.get(String.class), "description")
                          .addParameter(TypeName.BOOLEAN, "hasExactValueMatching")
                          .addStatement("this.$N = $N", "description", "description")
                          .addStatement("this.$N = $N", "hasExactValueMatching", "hasExactValueMatching")
                          .build();

        terminalTypeInterfaceTypeSpec.addMethod(constructorMethodSpec);

        MethodSpec getDescriptionMethodSpec =
                MethodSpec.methodBuilder("getDescription")
                          .addModifiers(Modifier.PUBLIC)
                          .returns(ClassName.get(String.class))
                          .addStatement("return $N", "description")
                          .build();

        terminalTypeInterfaceTypeSpec.addMethod(getDescriptionMethodSpec);

        MethodSpec hasExactTerminalMatchingMethodSpec =
                MethodSpec.methodBuilder("hasExactValueMatching")
                          .addModifiers(Modifier.PUBLIC)
                          .returns(TypeName.BOOLEAN)
                          .addStatement("return $N", "hasExactValueMatching")
                          .build();

        terminalTypeInterfaceTypeSpec.addMethod(hasExactTerminalMatchingMethodSpec);

        return terminalTypeInterfaceTypeSpec.build();
    }

    private TypeSpec generateEnumEntryParameter(String description, boolean hasExactTokenMatching) {
        return TypeSpec.anonymousClassBuilder("$S, $L", description, String.valueOf(hasExactTokenMatching)).build();
    }

}
