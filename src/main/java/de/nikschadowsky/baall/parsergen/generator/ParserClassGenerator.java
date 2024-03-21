package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.*;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.analysis.GrammarProductionTreeNode;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Queue;

/**
 * File created on 10.03.2024
 */
public class ParserClassGenerator implements JavaSourceGenerator {

    private static final String PARSING_METHOD_NAME_PREFIX = "parse";

    private static ParserClassGenerator INSTANCE;

    public static ParserClassGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ParserClassGenerator();
        }

        return INSTANCE;
    }

    private ParserClassGenerator() {
    }

    @Override
    public @NotNull TypeSpec generateTypeSpec(@NotNull Grammar grammar) {
        TypeSpec.Builder parserClassTypeSpec =
                TypeSpec.classBuilder(PARSER_CLASS_TYPENAME).addModifiers(Modifier.PUBLIC);


        return null;
    }

    private MethodSpec generateParserMethodForNonterminal(GrammarNonterminal nonterminal) {
        ParameterSpec terminalQueueParameter =
                ParameterSpec.builder(ParameterizedTypeName.get(
                        ClassName.get(Queue.class),
                        TERMINAL_COMPARABLE_INTERFACE_TYPENAME
                ), "tokens").build();

        MethodSpec.Builder methodSpec =
                MethodSpec.methodBuilder(getParseMethodName(nonterminal)).returns(TypeName.BOOLEAN)
                          .addParameter(terminalQueueParameter);


        return methodSpec.build();
    }

    @NotNull
    private static String getParseMethodName(GrammarNonterminal nonterminal) {
        return PARSING_METHOD_NAME_PREFIX + CaseUtils.toCamelCase(
                nonterminal.getIdentifier(),
                true,
                '_'
        );
    }


     public CodeBlock generateConditionCodeBlock(GrammarProductionTreeNode.Root root) {
        CodeBlock.Builder builder = CodeBlock.builder();
        List<GrammarProductionTreeNode.SymbolNode> children = root.getChildren();

        for (int i = 0; i < children.size(); i++) {
            builder.add(generateCodeForCondition(children.get(i), i == 0));
        }

        return builder.build();
    }

    private CodeBlock generateCodeForCondition(GrammarProductionTreeNode.SymbolNode conditionNode, boolean isFirst) {
        CodeBlock.Builder builder = CodeBlock.builder();

        String condition = (
                conditionNode.getValue().isTerminal()
                        ? "TERMINAL_MAP.get(\"%s\").symbolMatches(\"queue.remove()\")"
                        : "%s()"
        ).formatted(conditionNode.getValue().getFormatted());

        if (isFirst) {
            builder.beginControlFlow("if (%s)".formatted(condition));
        } else {
            builder.nextControlFlow("else if (%s)".formatted(condition));
        }

        List<GrammarProductionTreeNode.SymbolNode> children = conditionNode.getChildren();
        for (int i = 0; i < children.size(); i++) {
            builder.add(generateCodeForCondition(children.get(i), i == 0));
        }

        if (children.isEmpty()) {
            builder.addStatement("return true");
        }
        builder.endControlFlow();

        return builder.build();
    }


}
