package de.nikschadowsky.baall.parsergen.grammar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * File created on 02.01.2024
 */
class GrammarProductionTest {

    @Test
    void testEquals() {

        GrammarNonterminal lss = new GrammarNonterminal("A");

        GrammarProduction a1 =
                new GrammarProduction(0,
                                           lss,
                                           new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "A"),
                                           lss,
                                           new GrammarTerminal(
                                                   GrammarTerminal.TerminalType.ANY, "A")
        );
        GrammarProduction a2 =
                new GrammarProduction(1,
                                                     lss,
                                                     new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "A"),
                                                     lss,
                                                     new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "A")
        );

        GrammarProduction b = new GrammarProduction(2, lss, lss, new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "A"));
        GrammarProduction c = new GrammarProduction(3, lss, new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "A"), lss);


        assertEquals(a1, a2);
        assertNotEquals(a1, b);
        assertNotEquals(a1, c);

    }
}