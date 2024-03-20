package de.nikschadowsky.baall.parsergen.grammar.optimization;

import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * File created on 30.01.2024
 */
public class GrammarMinimizer {

    public Grammar minimizeGrammar(Grammar source) {
        Map<GrammarNonterminal, GrammarNonterminal>
                nonterminalTranslationMap = createNonterminalMapping(source);

        // create a new set of nonterminals with unfilled rule sets
        Map<GrammarNonterminal, GrammarBuilder.GrammarNonterminalBuilder> nonterminalBuilderMap
                = nonterminalTranslationMap.keySet()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        nonterminal -> new GrammarBuilder.GrammarNonterminalBuilder(
                                nonterminal,
                                nonterminalTranslationMap
                        )
                ));

        int totalNewNonterminals;

        do {
            totalNewNonterminals = nonterminalBuilderMap.size();

            Iterator<Map.Entry<GrammarNonterminal, GrammarBuilder.GrammarNonterminalBuilder>> itr =
                    nonterminalBuilderMap.entrySet().iterator();

            while (itr.hasNext()) {
                Map.Entry<GrammarNonterminal, GrammarBuilder.GrammarNonterminalBuilder> entry = itr.next();
                GrammarNonterminal nonterminal = entry.getKey();
                GrammarBuilder.GrammarNonterminalBuilder builder = nonterminalBuilderMap.get(nonterminal);


                if (builder.getRuleBuilder().hasAllTerminalProductions()) {
                    GrammarNonterminal toBeReplaced = nonterminalTranslationMap.get(nonterminal);


                    for (GrammarBuilder.GrammarNonterminalBuilder innerBuilder : nonterminalBuilderMap.values()) {
                        if (innerBuilder.getRuleBuilder().containsNonterminal(toBeReplaced)) {
                            innerBuilder.getRuleBuilder()
                                    .replace(toBeReplaced, builder.getRuleBuilder().getSententialForms());
                        }
                    }
                    if (!builder.isStartNonterminalBuilder())
                        itr.remove();
                }
            }


        } while (totalNewNonterminals != nonterminalBuilderMap.size());

        List<GrammarNonterminal> newNonterminals =
                nonterminalBuilderMap.values().stream()
                        .map(GrammarBuilder.GrammarNonterminalBuilder::build)
                        .toList();


        List<GrammarProduction> newProductionRules = new ArrayList<>();
        newNonterminals.stream().map(GrammarNonterminal::getProductionRules).forEach(newProductionRules::addAll);

        return new Grammar(newNonterminals.stream()
                .filter(nonterminal -> nonterminal.hasAnnotation("Start"))
                .findFirst()
                .orElseThrow(), newNonterminals, newProductionRules);
    }


    private static @NotNull Map<GrammarNonterminal, GrammarNonterminal> createNonterminalMapping(@NotNull Grammar source) {
        return source.getAllNonterminals()
                .stream()
                .collect(Collectors.toUnmodifiableMap(
                        Function.identity(),
                        n -> new GrammarNonterminal(
                                n.getIdentifier())
                ));
    }

}
