package de.nikschadowsky.baall.parsergen.grammar.optimization;

import de.nikschadowsky.baall.parsergen._utility.GrammarUtility;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import de.nikschadowsky.baall.parsergen.grammar.GrammarSymbol;
import de.nikschadowsky.baall.parsergen.grammar.generation.GrammarReader;
import de.nikschadowsky.baall.parsergen.util.FileUtility;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.nikschadowsky.baall.parsergen._utility.Assertions.assertCollectionShallowEquals;
import static de.nikschadowsky.baall.parsergen._utility.GrammarUtility.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * File created on 07.02.2024
 */
class GrammarBuilderTest {

    static Grammar grammar;
    static Map<GrammarNonterminal, GrammarNonterminal> nonterminalTranslationMap;

    @BeforeAll
    static void setupAll() {
        grammar = GrammarReader.getInstance().generateGrammar(FileUtility.getPathFromClasspath("GrammarBuilderTestGrammar.grammar"));
        nonterminalTranslationMap = grammar.getAllNonterminals()
                                           .stream()
                                           .collect(Collectors.toUnmodifiableMap(
                                                   Function.identity(),
                                                   nonterminal -> new GrammarNonterminal(nonterminal.getIdentifier())
                                           ));
    }

    @Test
    void testProductionRuleBuilderHasAllTerminalProductions() {
        assertTrue(getProductionRuleBuilder("A").hasAllTerminalProductions());
        assertFalse(getProductionRuleBuilder("B").hasAllTerminalProductions());
        assertTrue(getProductionRuleBuilder("C").hasAllTerminalProductions());
        assertTrue(getProductionRuleBuilder("D").hasAllTerminalProductions());
        assertFalse(getProductionRuleBuilder("E").hasAllTerminalProductions());
        assertTrue(getProductionRuleBuilder("F").hasAllTerminalProductions());

    }


    @ParameterizedTest
    @MethodSource("testProductionRuleBuilderReplaceArgumentProvider")
    void testProductionRuleBuilderBuild(String sourceId, String targetId, Set<List<GrammarSymbol>> replacements, Set<List<GrammarSymbol>> expectedRaw) {
        GrammarNonterminal sourceOld = getNonterminal(grammar, sourceId);
        GrammarNonterminal target = getNewNonterminal(targetId);

        Set<GrammarProduction> expected = expectedRaw.stream()
                                                     .map(raw -> GrammarUtility.createProductionRule(
                                                             raw.toArray(GrammarSymbol[]::new)
                                                     )).collect(Collectors.toSet());

        GrammarBuilder.ProductionRuleBuilder builder =
                new GrammarBuilder.ProductionRuleBuilder(sourceOld.getProductionRules(), nonterminalTranslationMap);

        builder.replace(target, replacements);

        List<GrammarProduction> build = builder.build();

        assertCollectionShallowEquals(expected, build);
    }

    @Test
    void testProductionRuleBuilderReplace() {
        GrammarBuilder.ProductionRuleBuilder builder = getProductionRuleBuilder("A");

        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> builder.replace(getNewNonterminal("A"), Collections.emptySet())
        );

        assertEquals(exception.getMessage(), "Replacement set cannot be empty!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B"})
    void testGrammarNonterminalBuilder(String id) {
        Map<GrammarNonterminal, GrammarNonterminal> nonterminalTranslationMap = new HashMap<>();
        GrammarNonterminal expected = getNonterminal(grammar, id);

        nonterminalTranslationMap.put(expected, new GrammarNonterminal(id));

        GrammarBuilder.GrammarNonterminalBuilder builder =
                new GrammarBuilder.GrammarNonterminalBuilder(expected, nonterminalTranslationMap);

        // assert that production rules are not locked in yet
        assertFalse(nonterminalTranslationMap.get(expected).areProductionRulesSet());
        // assert annotations were set correctly and are as expected
        assertTrue(nonterminalTranslationMap.get(expected).areAnnotationsSet());
        assertEquals(expected.getAnnotations(), nonterminalTranslationMap.get(expected).getAnnotations());

        GrammarNonterminal actual = builder.build();

        // rules should be set now
        assertTrue(actual.areProductionRulesSet());

        assertEquals(expected.getIdentifier(), actual.getIdentifier());
        assertCollectionShallowEquals(expected.getProductionRules(), actual.getProductionRules());
    }

    // TODO prettify!
    private static Stream<Arguments> testProductionRuleBuilderReplaceArgumentProvider() {
        return Stream.of(
                Arguments.of("B", "B", Set.of(getTerminalList("x")), Set.of(getTerminalList("x"))),
                Arguments.of("B", "B", Set.of(getTerminalList("x", "y")), Set.of(getTerminalList("x", "y"))),
                Arguments.of("B", "B", Set.of(getTerminalList()), Set.of(getTerminalList())),
                Arguments.of(
                        "B",
                        "B",
                        Set.of(getTerminalList("x"), getTerminalList("y")),
                        Set.of(getTerminalList("x"), getTerminalList("y"))
                ),
                Arguments.of("C", "B", Set.of(getTerminalList("x")), Set.of(getTerminalList("?", "?"))),
                Arguments.of("D", "B", Set.of(getTerminalList("x")), Set.of(getTerminalList("?"), getTerminalList("!"))),
                Arguments.of(
                        "E",
                        "B",
                        Set.of(getTerminalList("x")),
                        Set.of(
                                getTerminalList("?"),
                                List.of(
                                        getTerminalWithTypeAny("?"),
                                        nonterminalTranslationMap.get(getNonterminal(grammar, "E"))
                                )
                        )
                ),
                Arguments.of(
                        "E",
                        "E",
                        Set.of(getTerminalList("x")),
                        Set.of(getTerminalList("?"), getTerminalList("?", "x"))
                ),
                Arguments.of(
                        "E",
                        "E",
                        Set.of(getTerminalList("x", "y")),
                        Set.of(getTerminalList("?"), getTerminalList("?", "x", "y"))
                ),
                Arguments.of(
                        "E",
                        "E",
                        Set.of(getTerminalList("x"), getTerminalList("y")),
                        Set.of(getTerminalList("?"), getTerminalList("?", "x"), getTerminalList("?", "y"))
                ),
                Arguments.of("F", "F", Set.of(getTerminalList("x")), Set.of(getTerminalList("?"), getTerminalQueue())),
                Arguments.of("G", "G", Set.of(getTerminalList("x")), Set.of(getTerminalQueue()))
        );
    }

    // utility

    GrammarBuilder.ProductionRuleBuilder getProductionRuleBuilder(String nonterminalIdentifier) {
        return new GrammarBuilder.ProductionRuleBuilder(getNonterminal(grammar, nonterminalIdentifier)
                                                                .getProductionRules(), nonterminalTranslationMap);
    }

    @NotNull GrammarNonterminal getNewNonterminal(String oldId) {
        return getNewNonterminal(getNonterminal(grammar, oldId));
    }

    @NotNull GrammarNonterminal getNewNonterminal(GrammarNonterminal old) {
        return nonterminalTranslationMap.get(old);
    }

}