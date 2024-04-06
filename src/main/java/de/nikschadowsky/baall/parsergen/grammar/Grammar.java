package de.nikschadowsky.baall.parsergen.grammar;

import de.nikschadowsky.baall.parsergen.util.CollectionUtility;
import de.nikschadowsky.baall.parsergen.util.ImmutableLinkedHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.LinkedHashSet;
import java.util.Objects;

public class Grammar {


    private final GrammarNonterminal start;

    private final LinkedHashSet<GrammarNonterminal> nonterminals;

    private final LinkedHashSet<GrammarProduction> grammarProductions;

    private final LinkedHashSet<GrammarTerminal> terminals;

    public Grammar(@NotNull GrammarNonterminal start, @NotNull LinkedHashSet<GrammarNonterminal> nonterminals, @NotNull LinkedHashSet<GrammarProduction> grammarProductions, @NotNull LinkedHashSet<GrammarTerminal> terminals) {
        this.start = start;
        this.nonterminals = nonterminals;
        this.grammarProductions = grammarProductions;
        this.terminals = terminals;
    }

    public @NotNull GrammarNonterminal getStart() {
        return start;
    }

    public @Unmodifiable LinkedHashSet<GrammarNonterminal> getAllNonterminals() {
        return ImmutableLinkedHashSet.createFrom(nonterminals);
    }

    public @Unmodifiable LinkedHashSet<GrammarTerminal> getAllTerminals() {
        return ImmutableLinkedHashSet.createFrom(terminals);
    }

    @Override
    public String toString() {
        return "Grammar{%n     %s%n}".formatted(String.join(
                "\n     ",
                nonterminals.stream()
                            .map(GrammarNonterminal::toString)
                            .toList()
        ));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Grammar other) {
            return start.symbolDeepEquals(other.start) && CollectionUtility.compareCollections(
                    nonterminals,
                    other.nonterminals,
                    GrammarNonterminal::symbolDeepEquals
            ) && CollectionUtility.shallowCompareCollections(
                    terminals,
                    other.terminals
            ) && CollectionUtility.shallowCompareCollections(grammarProductions, other.grammarProductions);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, nonterminals, grammarProductions);
    }
}
