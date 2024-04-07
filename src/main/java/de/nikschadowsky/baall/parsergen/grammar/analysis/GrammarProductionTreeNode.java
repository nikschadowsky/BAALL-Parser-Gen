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

    protected final List<SymbolNode> children = new ArrayList<>();

    public SymbolNode addUniqueChild(@NotNull GrammarSymbol symbol, boolean isFinal) {
        Optional<SymbolNode> optExistingNode =
                children.stream().filter(symbolNode -> symbol.symbolDeepEquals(symbolNode.getValue())).findAny();

        if (optExistingNode.isEmpty()) {
            SymbolNode e = new SymbolNode(symbol);
            if (isFinal) {
                e.setFinal();
            }
            children.add(e);
            return e;
        }
        SymbolNode node = optExistingNode.get();
        if (isFinal) {
            node.setFinal();
        }
        return node;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrammarProductionTreeNode node) {
            return children.equals(node.children);
        }

        return false;
    }

    public @Unmodifiable List<SymbolNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public static class SymbolNode extends GrammarProductionTreeNode {

        private final GrammarSymbol value;

        private boolean isFinal;

        public SymbolNode(@NotNull GrammarSymbol value) {
            this.value = value;
        }

        public GrammarSymbol getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SymbolNode node) {
                return value.symbolDeepEquals(node.value) &&
                        isFinal == node.isFinal &&
                        super.equals(obj);
            }

            return false;
        }

        public void setFinal() {
            isFinal = true;
        }

        public boolean isFinal() {
            return isFinal;
        }
    }

    public static class Root extends GrammarProductionTreeNode {

        private final boolean hasEpsilonRule;

        public Root(boolean hasEpsilonRule) {
            this.hasEpsilonRule = hasEpsilonRule;
        }

        public boolean hasEpsilonRule() {
            return hasEpsilonRule;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof GrammarProductionTreeNode.Root root) {
                return hasEpsilonRule == root.hasEpsilonRule() && super.equals(root);
            }

            return false;
        }
    }

}
