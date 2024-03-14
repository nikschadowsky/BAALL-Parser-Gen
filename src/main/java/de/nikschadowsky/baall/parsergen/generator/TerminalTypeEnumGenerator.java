package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;
import org.jetbrains.annotations.NotNull;

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
                TypeSpec.enumBuilder(TERMINAL_TYPE_INTERFACE_TYPENAME).addModifiers(Modifier.PUBLIC);

        List<GrammarTerminal.TerminalType> entries = List.of(GrammarTerminal.TerminalType.values());

        entries.forEach(entry -> terminalTypeInterfaceTypeSpec.addEnumConstant(
                entry.name(),
                generateEnumEntryParameter(
                        entry.getDescription(),
                        entry.hasExactValueMatching()
                )
        ));

        MethodSpec constructorMethodSpec =
                MethodSpec.constructorBuilder()
                          .addParameter(ClassName.get(String.class), "description")
                          .addParameter(TypeName.BOOLEAN, "hasExactValueMatching")
                          .addStatement("this.$N = $N", "description", "description")
                          .addStatement("this.$N = $N", "hasExactValueMatching", "hasExactValueMatching")
                          .build();

        MethodSpec getDescriptionMethodSpec =
                MethodSpec.methodBuilder("getDescription")
                          .addModifiers(Modifier.PUBLIC)
                          .returns(ClassName.get(String.class))
                          .addStatement("return $N", "description")
                          .build();

        MethodSpec hasExactTerminalMatchingMethodSpec =
                MethodSpec.methodBuilder("hasExactValueMatching")
                          .addModifiers(Modifier.PUBLIC)
                          .returns(TypeName.BOOLEAN)
                          .addStatement("return $N", "hasExactValueMatching")
                          .build();

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

        terminalTypeInterfaceTypeSpec.addMethod(constructorMethodSpec);
        terminalTypeInterfaceTypeSpec.addMethod(getDescriptionMethodSpec);
        terminalTypeInterfaceTypeSpec.addMethod(hasExactTerminalMatchingMethodSpec);

        return terminalTypeInterfaceTypeSpec.build();
    }

    private TypeSpec generateEnumEntryParameter(String description, boolean hasExactTokenMatching) {
        return TypeSpec.anonymousClassBuilder("$S, $L", description, String.valueOf(hasExactTokenMatching)).build();
    }

}
