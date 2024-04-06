package de.nikschadowsky.baall.parsergen.grammar;

import de.nikschadowsky.baall.parsergen._utility.GrammarUtility;
import de.nikschadowsky.baall.parsergen.grammar.generation.GrammarReader;
import de.nikschadowsky.baall.parsergen.util.FileUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.nikschadowsky.baall.parsergen._utility.Assertions.assertCollectionShallowEquals;
import static org.junit.jupiter.api.Assertions.*;

class GrammarReaderTest {

    @Test
    void testCreateGrammar() {
        Grammar g = GrammarReader.getInstance()
                                 .generateGrammar(FileUtility.getPathFromClasspath("GrammarReaderTestFile.grammar"));

        assertTrue(g.getStart().getIdentifier().equalsIgnoreCase("start"));

        Set<String> testNonterminals = Set.of("START", "A", "B", "END");
        assertTrue(g.getAllNonterminals().stream().allMatch(elem -> testNonterminals.contains(elem.getIdentifier())));

        Set<Map.Entry<String, GrammarTerminal.TerminalType>> testTerminals = Set.of(
                Map.entry("T", GrammarTerminal.TerminalType.ANY),
                Map.entry("|", GrammarTerminal.TerminalType.ANY),
                Map.entry("", GrammarTerminal.TerminalType.NUMBER)
        );

        assertCollectionShallowEquals(
                testTerminals,
                g.getAllTerminals()
                 .stream()
                 .map(t -> Map.entry(t.value(), t.type()))
                 .collect(Collectors.toSet())
        );


        assertEquals(4, g.getAllNonterminals().size());

        Assertions.assertTrue(GrammarUtility.getNonterminal(g, "START")
                                            .getAnnotations()
                                            .contains(new GrammarNonterminalAnnotation("StartAnnotation")));
        assertTrue(GrammarUtility.getNonterminal(g, "END")
                                 .getAnnotations()
                                 .contains(new GrammarNonterminalAnnotation("EndAnnotation")));
        assertTrue(GrammarUtility.getNonterminal(g, "END")
                                 .getAnnotations()
                                 .contains(new GrammarNonterminalAnnotation("AdditionalAnnotation")));
        assertEquals(2, GrammarUtility.getNonterminal(g, "END").getAnnotations().size());

        String derivationRepresentation = g.getStart().getProductionRules().toString();

        String productionA = "A B \"T\" \"|\"";
        String productionB = "ε";

        System.out.println(derivationRepresentation);

        assertTrue(derivationRepresentation.contains(productionA) && derivationRepresentation.contains(productionB));
    }

    @Test
    void testCreateGrammarWithStartNotInFirstLine() {
        Grammar g = GrammarReader.getInstance()
                                 .generateGrammar(FileUtility.getPathFromClasspath(
                                         "GrammarReaderStartInLastLineTestFile.grammar"));

        assertEquals(GrammarUtility.getNonterminal(g, "B"), g.getStart());
        assertTrue(GrammarUtility.getNonterminal(g, "START").getAnnotations().isEmpty());
    }

    @Test
    void testCreateGrammarWithMultipleAnnotations() {
        Grammar g = GrammarReader.getInstance()
                                 .generateGrammar(FileUtility.getPathFromClasspath(
                                         "GrammarReaderNonterminalWithMultipleAnnotationsTestFile.grammar"));

        assertEquals(GrammarUtility.getNonterminal(g, "A"), g.getStart());
        assertEquals(4, g.getStart().getAnnotations().size());
    }

    @Test
    void testCreateGrammarSyntaxError() {
        Exception e = assertThrows(
                GrammarSyntaxException.class,
                () -> GrammarReader.getInstance()
                                   .generateGrammar(FileUtility.getPathFromClasspath(
                                           "GrammarReaderTestSyntaxError.grammar"))
        );

        String expected = "Missing symbols";

        System.err.println(e.getMessage());

        assertTrue(e.getMessage().contains(expected));
    }

    @Test
    void testCreateGrammarEpsilonError() {
        Exception e = assertThrows(
                GrammarSyntaxException.class,
                () -> GrammarReader.getInstance()
                                   .generateGrammar(FileUtility.getPathFromClasspath(
                                           "GrammarReaderTestEpsilonError.grammar"))
        );

        String expected = "Meta symbols cannot be used as identifiers for nonterminals!";

        System.err.println(e.getMessage());

        assertTrue(e.getMessage().contains(expected));
    }
}