package de.nikschadowsky.baall.parsergen.grammar.analysis;

import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import de.nikschadowsky.baall.parsergen.grammar.GrammarSymbol;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 15.03.2024
 */
public class GrammarProductionTreeAssembler {
    public static @NotNull GrammarProductionTreeNode.Root generateConditionTree(@NotNull GrammarNonterminal nonterminal) {
        boolean hasEpsilonRule =
                nonterminal.getProductionRules().stream().anyMatch(GrammarProduction::isEpsilonProduction);

        GrammarProductionTreeNode.Root root = new GrammarProductionTreeNode.Root(hasEpsilonRule);

        for (GrammarProduction rule : nonterminal.getProductionRules()) {
            GrammarProductionTreeNode current = root;

            GrammarSymbol[] sententialForm = rule.getSententialForm();
            for (GrammarSymbol symbol : sententialForm) {
                current = current.addUniqueChild(symbol);
            }
        }

        return root;
    }

}
