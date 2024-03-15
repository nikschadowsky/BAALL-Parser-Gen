package de.nikschadowsky.baall.parsergen.grammar.analysis;

import de.nikschadowsky.baall.parsergen.grammar.GrammarSymbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * File created on 15.03.2024
 */
public abstract class GrammarProductionTreeNode {

    private final List<SymbolNode> children = new ArrayList<>();

    public SymbolNode addUniqueChild(@NotNull GrammarSymbol symbol) {
        Optional<SymbolNode> optExistingNode =
                children.stream().filter(symbolNode -> symbol.symbolDeepEquals(symbolNode.getValue())).findAny();

        if (optExistingNode.isEmpty()) {
            SymbolNode e = new SymbolNode(symbol);
            children.add(e);
            return e;
        }

        return optExistingNode.get();
    }

    public @Unmodifiable List<SymbolNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public static class SymbolNode extends GrammarProductionTreeNode {

        private final GrammarSymbol value;

        public SymbolNode(@NotNull GrammarSymbol value) {
            this.value = value;
        }

        public GrammarSymbol getValue() {
            return value;
        }

    }

    public static class Root extends GrammarProductionTreeNode {

        private final List<SymbolNode> topLevelChildren = new ArrayList<>();

        private final boolean hasEpsilonRule;

        public Root(boolean hasEpsilonRule) {
            this.hasEpsilonRule = hasEpsilonRule;
        }

        public boolean hasEpsilonRule() {
            return hasEpsilonRule;
        }

    }

}
