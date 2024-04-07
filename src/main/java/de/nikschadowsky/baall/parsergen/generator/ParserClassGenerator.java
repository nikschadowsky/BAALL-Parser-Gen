package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.*;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;
import de.nikschadowsky.baall.parsergen.grammar.analysis.GrammarProductionTreeAssembler;
import de.nikschadowsky.baall.parsergen.grammar.analysis.GrammarProductionTreeNode;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

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

        AnnotationSpec generatedAnnotationSpec = AnnotationSpec.builder(Generated.class).addMember("value", "$S", "by BAALL-Parser-Gen").build();

        parserClassTypeSpec.addAnnotation(generatedAnnotationSpec);

        parserClassTypeSpec.addField(ParameterizedTypeName.get(
                ClassName.get(Queue.class),
                TERMINAL_COMPARABLE_INTERFACE_TYPENAME
        ), "queue", Modifier.PRIVATE);

        FieldSpec terminalMap = FieldSpec.builder(ParameterizedTypeName.get(
                                                 ClassName.get(Map.class),
                                                 ClassName.get(String.class),
                                                 JavaSourceGenerator.TERMINAL_CLASS_TYPENAME
                                         ), "TERMINAL_MAP", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                                         .initializer("generateMapEntries()")
                                         .build();

        parserClassTypeSpec.addField(terminalMap);

        parserClassTypeSpec.addMethod(generateMapEntryMethod(grammar.getAllTerminals()));

        parserClassTypeSpec.addMethod(generateConstructor());

        parserClassTypeSpec.addMethods(grammar.getAllNonterminals()
                                              .stream()
                                              .map(this::generateParserMethodForNonterminal)
                                              .collect(
                                                      Collectors.toList()));

        parserClassTypeSpec.addType(TerminalSymbolClassGenerator.getInstance().generateTypeSpec(grammar));

        return parserClassTypeSpec.build();
    }


    protected MethodSpec generateConstructor() {
        ParameterSpec terminalQueueParameterSpec = ParameterSpec.builder(ParameterizedTypeName.get(
                ClassName.get(Queue.class),
                TERMINAL_COMPARABLE_INTERFACE_TYPENAME
        ), "tokens").build();

        return MethodSpec.constructorBuilder()
                         .addModifiers(Modifier.PUBLIC)
                         .addParameter(terminalQueueParameterSpec)
                         .addStatement("this.queue = tokens")
                         .build();
    }

    protected MethodSpec generateParserMethodForNonterminal(GrammarNonterminal nonterminal) {
        ParameterSpec terminalQueueParameterSpec =
                ParameterSpec.builder(ParameterizedTypeName.get(
                        ClassName.get(Queue.class),
                        TERMINAL_COMPARABLE_INTERFACE_TYPENAME
                ), "queue").build();

        CodeBlock conditionsCodeBlock =
                generateCodeBlockForConditionTree(GrammarProductionTreeAssembler.generateConditionTree(nonterminal));

        return MethodSpec.methodBuilder(getParseMethodName(nonterminal))
                         .addModifiers(Modifier.PRIVATE)
                         .returns(TypeName.BOOLEAN)
                         .addParameter(terminalQueueParameterSpec)
                         .addCode(conditionsCodeBlock)
                         .build();
    }

    private CodeBlock generateCodeBlockForConditionTree(GrammarProductionTreeNode.Root root) {
        CodeBlock.Builder builder = CodeBlock.builder();
        List<GrammarProductionTreeNode.SymbolNode> children = root.getChildren();

        for (int i = 0; i < children.size(); i++) {
            builder.add(generateCodeForCondition(children.get(i), i == 0));
        }
        if (root.hasEpsilonRule()) {
            builder.add("return true;\n");
        } else {
            builder.add("// TODO add behaviour when this symbol couldn't be parsed\n");
            builder.add("throw new RuntimeException(\"Expected '?' but got '?'!\");\n");
        }
        return builder.build();
    }

    private CodeBlock generateCodeForCondition(GrammarProductionTreeNode.SymbolNode conditionNode, boolean isFirst) {
        CodeBlock.Builder builder = CodeBlock.builder();

        String condition = (
                conditionNode.getValue().isTerminal()
                        ? "TERMINAL_MAP.get(\"%s\").symbolMatches(queue.poll())"
                        .formatted(conditionNode.getValue().getFormatted())
                        : "%s(queue)".formatted(getParseMethodName((GrammarNonterminal) conditionNode.getValue()))
        );

        if (isFirst) {
            builder.beginControlFlow("if (%s)".formatted(condition));
        } else {
            builder.beginControlFlow("else if (%s)".formatted(condition));
        }

        List<GrammarProductionTreeNode.SymbolNode> children = conditionNode.getChildren();
        for (int i = 0; i < children.size(); i++) {
            builder.add(generateCodeForCondition(children.get(i), i == 0));
        }

        if (children.isEmpty() || conditionNode.isFinal()) {
            builder.add("return true;\n");
        }
        builder.endControlFlow();

        return builder.build();
    }

    protected MethodSpec generateMapEntryMethod(LinkedHashSet<GrammarTerminal> terminals) {
        TypeName returnTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                TERMINAL_CLASS_TYPENAME
        );

        return MethodSpec.methodBuilder("generateMapEntries")
                         .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                         .returns(returnTypeName)
                         .addCode(generateCodeBlockForMapEntries(terminals)).build();
    }

    private CodeBlock generateCodeBlockForMapEntries(LinkedHashSet<GrammarTerminal> terminals) {
        if (terminals.isEmpty()) {
            return CodeBlock.builder().addStatement("return $T.ofEntries()", Map.class).build();
        }

        StringBuilder mapDefinitionCodeBuilder = new StringBuilder("return $T.ofEntries(\n");

        List<String> entries = terminals.stream()
                                        .map(terminal -> "Map.entry(\"%s\", new %s(%s.%s, \"%s\"))".formatted(
                                                terminal.value(),
                                                JavaSourceGenerator.TERMINAL_CLASS_TYPENAME.simpleName(),
                                                JavaSourceGenerator.TERMINAL_TYPE_ENUM_TYPENAME.simpleName(),
                                                terminal.type().name(),
                                                terminal.value()
                                        )).toList();

        String entryDefinitions = String.join(",\n", entries);

        mapDefinitionCodeBuilder.append(entryDefinitions);
        mapDefinitionCodeBuilder.append("\n)");

        return CodeBlock.builder().addStatement(mapDefinitionCodeBuilder.toString(), Map.class).build();
    }

    public static String getParseMethodName(GrammarNonterminal nonterminal) {
        return PARSING_METHOD_NAME_PREFIX + toPascalCase(nonterminal.getIdentifier());
    }

    public static String toPascalCase(String string) {
        String[] parts = string.split("_+");

        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            builder.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
        }

        return builder.toString();
    }

}
