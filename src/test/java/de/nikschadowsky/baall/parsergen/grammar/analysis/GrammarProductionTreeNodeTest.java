package de.nikschadowsky.baall.parsergen.grammar.analysis;

import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File created on 15.03.2024
 */
class GrammarProductionTreeNodeTest {

    @Test
    void testAddUniqueChild() {
        WrapperNode node = new WrapperNode();

        GrammarTerminal t1 = new GrammarTerminal(GrammarTerminal.TerminalType.STRING, "myValue");
        GrammarTerminal t2 = new GrammarTerminal(GrammarTerminal.TerminalType.STRING, "myValue");
        GrammarTerminal t3 = new GrammarTerminal(GrammarTerminal.TerminalType.STRING, "myValue2");

        // validate test data
        assertTrue(t1.symbolDeepEquals(t2));
        assertFalse(t1.symbolDeepEquals(t3));
        assertFalse(t2.symbolDeepEquals(t3));

        node.addUniqueChild(t1);
        assertEquals(1, node.getChildren().size());
        node.addUniqueChild(t1);
        // adding the same element should not affect size
        assertEquals(1, node.getChildren().size());

        // adding an element considered equal should not affect size
        node.addUniqueChild(t2);
        assertEquals(1, node.getChildren().size());

        // but adding an element considered NOT equal should increase the size by one
        node.addUniqueChild(t3);
        assertEquals(2, node.getChildren().size());


    }


    private static class WrapperNode extends GrammarProductionTreeNode {

    }
}