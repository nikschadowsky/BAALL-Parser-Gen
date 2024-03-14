package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.*;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import de.nikschadowsky.baall.parsergen.grammar.GrammarSymbol;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.lang.model.element.Modifier;
import java.util.*;

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

    private Node generateConditionTree(GrammarNonterminal nonterminal) {
        Node root = Node.ROOT;

        for (GrammarProduction rule : nonterminal.getProductionRules()) {
            Node current = root;

            GrammarSymbol[] sententialForm = rule.getSententialForm();
            for (GrammarSymbol symbol : sententialForm) {
                current = current.addUniqueChild(symbol);
            }
        }

        return root;
    }

    private CodeBlock generateConditionCodeBlock(Node root) {
        List<Node> children = root.getChildren();
        CodeBlock.Builder builder = CodeBlock.builder();

        for (int i = 0; i < children.size(); i++) {
            generateCodeForCondition(builder, children.get(i), i == 0);
        }

        builder.addStatement("return false");

        return builder.build();
    }

    private void generateCodeForCondition(CodeBlock.Builder codeBuilder, Node conditionNode, boolean isFirst) {
        String condition = (
                conditionNode.getValue().isTerminal()
                        ? "TERMINAL_MAP.get(\"%s\").symbolMatches(\"queue.remove()\")"
                        : "%s()"
        ).formatted(conditionNode.getValue().getFormatted());

        if (isFirst) {
            codeBuilder.beginControlFlow("if (%s)".formatted(condition));
        } else {
            codeBuilder.nextControlFlow("else if (%s)".formatted(condition));
        }

        List<Node> children = conditionNode.getChildren();
        for (int i = 0; i < children.size(); i++) {
            generateCodeForCondition(codeBuilder, children.get(i), i == 0);
        }

        if (children.isEmpty()) {
            codeBuilder.addStatement("return true");
        }

        codeBuilder.endControlFlow();
    }

    private static class Node {

        private final List<Node> children = new ArrayList<>();

        private final GrammarSymbol value;

        public static final Node ROOT = new Node();

        public Node(@NotNull GrammarSymbol value) {
            this.value = value;
        }

        private Node() {
            this.value = null;
        }

        Node addUniqueChild(@NotNull GrammarSymbol symbol) {
            Optional<Node> optExistingNode =
                    children.stream().filter(node -> symbol.symbolDeepEquals(node.getValue())).findAny();

            if (optExistingNode.isEmpty()) {
                Node e = new Node(symbol);
                children.add(e);
                return e;
            }

            return optExistingNode.get();
        }

        public GrammarSymbol getValue() {
            return value;
        }

        public @Unmodifiable List<Node> getChildren() {
            return Collections.unmodifiableList(children);
        }

    }
}
