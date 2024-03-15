package de.nikschadowsky.baall.parsergen.grammar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrammarSymbolTest {


    private GrammarTerminal terminalExactSameValue1, terminalExactSameValue2, terminalExactDiffValue1;
    private GrammarTerminal terminalNotExactSameValue1, terminalNotExactSameValue2, terminalNotExactDiffValue1;
    private GrammarTerminal terminalAny;

    private GrammarNonterminal A, B, C;

    @BeforeEach
    void setUp() {
        GrammarTerminal.TerminalType withExactValueMatching =
                Arrays.stream(GrammarTerminal.TerminalType.values())
                      .filter(GrammarTerminal.TerminalType::hasExactValueMatching)
                      .findAny()
                      .orElseThrow();

        terminalExactSameValue1 = new GrammarTerminal(withExactValueMatching, "same");
        terminalExactSameValue2 = new GrammarTerminal(withExactValueMatching, "same");
        terminalExactDiffValue1 = new GrammarTerminal(withExactValueMatching, "different");

        terminalNotExactSameValue1 = new GrammarTerminal(GrammarTerminal.TerminalType.STRING, "same");
        terminalNotExactSameValue2 = new GrammarTerminal(GrammarTerminal.TerminalType.BOOLEAN, "same");
        terminalNotExactDiffValue1 = new GrammarTerminal(GrammarTerminal.TerminalType.BOOLEAN, "different");

        terminalAny = new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "same");


        A = new GrammarNonterminal("identifier");
        B = new GrammarNonterminal("identifier");
        C = new GrammarNonterminal("different identifier");

    }

    @Test
    void testSymbolMatchesTokens() {
        // testing for reflexivity
        assertTrue(terminalExactSameValue1.symbolEquals(terminalExactSameValue1));
        assertTrue(terminalNotExactSameValue1.symbolEquals(terminalNotExactSameValue1));

        // testing exact value matching
        assertTrue(terminalExactSameValue1.symbolEquals(terminalExactSameValue2));
        assertTrue(terminalExactSameValue2.symbolEquals(terminalExactSameValue1));

        assertFalse(terminalExactDiffValue1.symbolEquals(terminalExactSameValue1));
        assertFalse(terminalExactSameValue2.symbolEquals(terminalExactDiffValue1));

        // testing not exact value matching
        assertTrue(terminalNotExactSameValue1.symbolEquals(terminalNotExactSameValue2));
        assertTrue(terminalNotExactSameValue2.symbolEquals(terminalNotExactSameValue1));

        assertFalse(terminalNotExactDiffValue1.symbolEquals(terminalNotExactSameValue1));
        assertFalse(terminalNotExactSameValue2.symbolEquals(terminalNotExactDiffValue1));

        // testing ANY should ignore type and just compare value
        assertTrue(terminalAny.symbolEquals(terminalExactSameValue1));
        assertTrue(terminalExactSameValue1.symbolEquals(terminalAny));
        assertTrue(terminalAny.symbolEquals(terminalNotExactSameValue1));
        assertTrue(terminalExactSameValue1.symbolEquals(terminalAny));

        // terminal and nonterminal should never equal each other
        assertFalse(A.symbolEquals(terminalAny));
        assertFalse(terminalAny.symbolEquals(B));
    }

    @Test
    void testSymbolMatchesNonterminal() {
        assertTrue(A.symbolEquals(B));
        assertTrue(B.symbolEquals(A));

        assertFalse(B.symbolEquals(C));
        assertFalse(C.symbolEquals(B));
    }

}
