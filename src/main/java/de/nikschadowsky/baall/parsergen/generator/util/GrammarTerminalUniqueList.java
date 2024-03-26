package de.nikschadowsky.baall.parsergen.generator.util;

import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GrammarTerminalUniqueList {

    private final List<GrammarTerminal> terminalList = new ArrayList<>();

    public void add(GrammarTerminal terminal) {
        if (terminalList.stream().noneMatch(t -> t.symbolDeepEquals(terminal))) {
            terminalList.add(terminal);
        }
    }

    public boolean isEmpty() {
        return terminalList.isEmpty();
    }

    public Stream<GrammarTerminal> stream() {
        return terminalList.stream();
    }

}
