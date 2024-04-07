package de.nikschadowsky.baall.parsergen.grammar.analysis;

import de.nikschadowsky.baall.parsergen._utility.GrammarUtility;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * File created on 15.03.2024
 */
class GrammarProductionTreeAssemblerTest {

    @ParameterizedTest
    @MethodSource("generateConditionTestProvider")
    void generateConditionTree(List<GrammarProduction> ruleset, GrammarProductionTreeNode.Root expected) {
        GrammarNonterminal nonterminal = new GrammarNonterminal("TEST");
        nonterminal.addProductionRules(ruleset);

        assertEquals(expected, GrammarProductionTreeAssembler.generateConditionTree(nonterminal));
    }

    static Stream<Arguments> generateConditionTestProvider() {
        return Stream.of(
                argumentsForEmpty(),
                argumentsForLinear(),
                argumentsForSingleBranching(),
                argumentsForDoubleBranching()
        );
    }

    private static Arguments argumentsForEmpty() {
        GrammarProduction empty = new GrammarProduction(-1);

        GrammarProductionTreeNode.Root expected = new GrammarProductionTreeNode.Root(true);

        return Arguments.of(List.of(empty), expected);

    }

    private static Arguments argumentsForLinear() {
        GrammarProduction linear = new GrammarProduction(-1,
                GrammarUtility.getTerminalList("a", "b", "c").toArray(new GrammarTerminal[0]));

        GrammarProductionTreeNode.Root expected = new GrammarProductionTreeNode.Root(false);

        expected.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("a"),false)
                .addUniqueChild(GrammarUtility.getTerminalWithTypeAny("b"),false)
                .addUniqueChild(GrammarUtility.getTerminalWithTypeAny("c"),true);

        return Arguments.of(List.of(linear), expected);
    }

    private static Arguments argumentsForSingleBranching() {
        GrammarTerminal first = GrammarUtility.getTerminalWithTypeAny("a");

        GrammarProduction left = new GrammarProduction(-1, first, GrammarUtility.getTerminalWithTypeAny("b1"));
        GrammarProduction middle = new GrammarProduction(-1, first, GrammarUtility.getTerminalWithTypeAny("b2"));
        GrammarProduction right = new GrammarProduction(-1, first, GrammarUtility.getTerminalWithTypeAny("b3"));

        GrammarProductionTreeNode.Root expected = new GrammarProductionTreeNode.Root(false);

        GrammarProductionTreeNode.SymbolNode firstNode = expected.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("a"), false);
        firstNode.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("b1"), true);
        firstNode.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("b2"), true);
        firstNode.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("b3"), true);

        return Arguments.of(List.of(left, middle, right), expected);
    }

    private static Arguments argumentsForDoubleBranching() {
        GrammarProduction left = new GrammarProduction(
                -1,
                GrammarUtility.getTerminalWithTypeAny("a"),
                GrammarUtility.getTerminalWithTypeAny("b1"),
                GrammarUtility.getTerminalWithTypeAny("c1")
        );

        GrammarProduction mid = new GrammarProduction(
                -1,
                GrammarUtility.getTerminalWithTypeAny("a"),
                GrammarUtility.getTerminalWithTypeAny("b2"),
                GrammarUtility.getTerminalWithTypeAny("c1")
        );

        GrammarProduction right = new GrammarProduction(
                -1,
                GrammarUtility.getTerminalWithTypeAny("a"),
                GrammarUtility.getTerminalWithTypeAny("b2"),
                GrammarUtility.getTerminalWithTypeAny("c2")
        );


        GrammarProductionTreeNode.Root expected = new GrammarProductionTreeNode.Root(false);
        GrammarProductionTreeNode.SymbolNode a = expected.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("a"), false);

        a.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("b1"), false).addUniqueChild(GrammarUtility.getTerminalWithTypeAny("c1"), true);

        GrammarProductionTreeNode.SymbolNode b2 = a.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("b2"), false);
        b2.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("c1"), true);
        b2.addUniqueChild(GrammarUtility.getTerminalWithTypeAny("c2"), true);

        return Arguments.of(List.of(left, mid, right), expected);
    }

}