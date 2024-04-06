package de.nikschadowsky.baall.parsergen._utility;

import de.nikschadowsky.baall.parsergen.grammar.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * File created on 13.01.2024
 */
public class GrammarUtility {

    public static GrammarNonterminal getNonterminal(Grammar grammar, String id) {
        return grammar.getAllNonterminals()
                      .stream()
                      .filter(g -> g.getIdentifier().equals(id))
                      .findFirst()
                      .orElseThrow();
    }

    public static Queue<GrammarTerminal> getTerminalQueue(String... tokenValues) {
        return Arrays.stream(tokenValues)
                     .map(GrammarUtility::getTerminalWithTypeAny)
                     .collect(Collectors.toCollection(LinkedList::new));
    }

    public static List<GrammarTerminal> getTerminalList(String... tokenValues) {
        return new ArrayList<>(getTerminalQueue(tokenValues));
    }

    public static GrammarTerminal getTerminalWithTypeAny(String value) {
        return new GrammarTerminal(GrammarTerminal.TerminalType.ANY, value);
    }

    public static GrammarProduction createProductionRule(GrammarSymbol... prod) {
        return new GrammarProduction(-1, prod);
    }
}
