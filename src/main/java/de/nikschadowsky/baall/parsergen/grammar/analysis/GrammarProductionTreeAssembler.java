package de.nikschadowsky.baall.parsergen.grammar.analysis;

import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import de.nikschadowsky.baall.parsergen.grammar.GrammarSymbol;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 15.03.2024
 */
public class GrammarProductionTreeAssembler {
    private GrammarProductionTreeAssembler() {
    }

    public static @NotNull GrammarProductionTreeNode.Root generateConditionTree(@NotNull GrammarNonterminal nonterminal) {
        boolean hasEpsilonRule =
                nonterminal.getProductionRules().stream().anyMatch(GrammarProduction::isEpsilonProduction);

        GrammarProductionTreeNode.Root root = new GrammarProductionTreeNode.Root(hasEpsilonRule);

        for (GrammarProduction rule : nonterminal.getProductionRules()) {
            GrammarProductionTreeNode current = root;

            GrammarSymbol[] sententialForm = rule.getSententialForm();
            for (int i = 0; i < sententialForm.length; i++) {
                current = current.addUniqueChild(sententialForm[i], i == sententialForm.length - 1);
            }
        }

        return root;
    }

}
