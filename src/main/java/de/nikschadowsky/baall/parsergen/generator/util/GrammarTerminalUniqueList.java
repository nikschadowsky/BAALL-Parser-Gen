package de.nikschadowsky.baall.parsergen.generator.util;

import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;

import java.util.ArrayList;
import java.util.List;

public class GrammarTerminalUniqueList {

    private final List<GrammarTerminal> terminalList = new ArrayList<>();

    private void add(GrammarTerminal terminal) {
        if (terminalList.stream().noneMatch(t -> t.symbolDeepEquals(terminal))){
            terminalList.add(terminal);
        }
    }

}
