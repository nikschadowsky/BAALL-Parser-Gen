package de.nikschadowsky.baall.parsergen.grammar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrammarSymbolTest {

    private GrammarTerminal token1, token2, token3, token4, token5, token6;

    private GrammarNonterminal A, B, C;

    @BeforeEach
    void setUp() {
        token1 = new GrammarTerminal(GrammarTerminal.TerminalType.STRING, "value");
        token2 = new GrammarTerminal(GrammarTerminal.TerminalType.KEYWORD, "same type a");
        token3 = new GrammarTerminal(GrammarTerminal.TerminalType.KEYWORD, "same type b");
        token4 = new GrammarTerminal(GrammarTerminal.TerminalType.STRING, "same value");
        token5 = new GrammarTerminal(GrammarTerminal.TerminalType.STRING, "same value");
        token6 = new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "same value");


        A = new GrammarNonterminal("identifier");
        B = new GrammarNonterminal("identifier");
        C = new GrammarNonterminal("different identifier");

    }

    @Test
    void testSymbolMatchesTokens() {
        // always check for reflexivity
        assertTrue(token4.symbolEquals(token5) && token5.symbolEquals(token4));
        assertTrue(token5.symbolEquals(token6) && token6.symbolEquals(token5));

        // also both directions on same type but different value
        assertFalse(token2.symbolEquals(token3) || token3.symbolEquals(token2));

        // for good measure
        assertFalse(token1.symbolEquals(token2));
        assertFalse(token1.symbolEquals(token3));
        assertFalse(token1.symbolEquals(token6));
        assertTrue(token1.symbolEquals(token4)); // Strings match regardless of content
        assertTrue(token1.symbolEquals(token5));

        // and just checking that one symbol matches itself
        assertTrue(token1.symbolEquals(token1));
    }

    @Test
    void testSymbolMatchesNonterminal() {
        // checking for reflexivity just in case String.equals isn't reflexive
        assertTrue(A.symbolEquals(B) && B.symbolEquals(A));

        assertFalse(B.symbolEquals(C) || C.symbolEquals(B));
    }

    @Test
    void testSymbolMatchesMixed() {
        assertFalse(A.symbolEquals(token1));
        assertFalse(token2.symbolEquals(B));

    }
}
