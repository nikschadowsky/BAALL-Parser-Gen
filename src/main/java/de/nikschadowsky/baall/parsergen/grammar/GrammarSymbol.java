package de.nikschadowsky.baall.parsergen.grammar;

import org.jetbrains.annotations.NotNull;

public interface GrammarSymbol {

    /**
     * Used to differentiate between Terminal Symbols and Nonterminal Symbols
     *
     * @return isTerminal
     */
    boolean isTerminal();

    /**
     * Compares two grammar symbols in a deep way. Check individual implementations!
     *
     * @return if symbols are deeply equal
     */
    boolean symbolDeepEquals(GrammarSymbol s);

    boolean equals(Object o);

    /**
     * @return a formatted representation of this symbol
     */
    @NotNull String getFormatted();

}
