package de.nikschadowsky.baall.parsergen.grammar;

import de.nikschadowsky.baall.parsergen._utility.GrammarUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File created on 25.01.2024
 */
class GrammarNonterminalTest {

    GrammarNonterminal nonterminal;

    @BeforeEach
    void setup() {
        nonterminal = new GrammarNonterminal("START");
    }

    @Test
    void testAddAnnotations() {
        List<GrammarNonterminalAnnotation> annotationSet1 = List.of(
                new GrammarNonterminalAnnotation("Annotation1"),
                new GrammarNonterminalAnnotation("Annotation2")
        );
        List<GrammarNonterminalAnnotation> annotationSet2 = List.of(
                new GrammarNonterminalAnnotation("Annotation3"),
                new GrammarNonterminalAnnotation("Annotation4")
        );

        assertTrue(nonterminal.addAnnotations(annotationSet1));
        assertFalse(nonterminal.addAnnotations(annotationSet2));

        assertEquals(annotationSet1, nonterminal.getAnnotations());
    }


    @Test
    void testHasAnnotations() {
        List<GrammarNonterminalAnnotation> annotationSet = List.of(
                new GrammarNonterminalAnnotation("Annotation1"),
                new GrammarNonterminalAnnotation("Annotation2")
        );
        nonterminal.addAnnotations(annotationSet);

        assertTrue(nonterminal.hasAnnotation("Annotation1"));
        assertTrue(nonterminal.hasAnnotation("Annotation2"));
        assertFalse(nonterminal.hasAnnotation("Annotation3"));
    }

    @Test
    void testAddProductionRules() {
        List<GrammarProduction> ruleSet1 = List.of(new GrammarProduction(
                0,
                nonterminal,
                GrammarUtility.getTerminalWithTypeAny("a"),
                GrammarUtility.getTerminalWithTypeAny("b")
        ));
        List<GrammarProduction> ruleSet2 = List.of(new GrammarProduction(
                0,
                nonterminal,
                GrammarUtility.getTerminalWithTypeAny("c"),
                GrammarUtility.getTerminalWithTypeAny("d")
        ));

        assertTrue(nonterminal.addProductionRules(ruleSet1));
        assertFalse(nonterminal.addProductionRules(ruleSet2));

        assertEquals(ruleSet1, nonterminal.getProductionRules());
    }
}
