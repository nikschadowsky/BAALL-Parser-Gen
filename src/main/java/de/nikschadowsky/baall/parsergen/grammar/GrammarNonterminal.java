package de.nikschadowsky.baall.parsergen.grammar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public class GrammarNonterminal implements GrammarSymbol {

    private final List<GrammarNonterminalAnnotation> annotations = new ArrayList<>();
    private final List<GrammarProduction> productionRules = new ArrayList<>();

    private final String identifier;

    private boolean areProductionRulesSet = false;
    private boolean areAnnotationsSet = false;

    public GrammarNonterminal(@NotNull String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Adds production rules to this nonterminal and sets them unmodifiable.
     *
     * @param newProductionRules production rules to add
     * @return whether this operation was successful
     */
    public boolean addProductionRules(List<GrammarProduction> newProductionRules) {
        if (!areProductionRulesSet) {
            productionRules.addAll(newProductionRules);
            areProductionRulesSet = true;
            return true;
        }
        return false;
    }


    /**
     * @return an immutable set of this nonterminal's production rules
     */
    public @Unmodifiable List<GrammarProduction> getProductionRules() {
        return Collections.unmodifiableList(productionRules);
    }

    public boolean areProductionRulesSet() {
        return areProductionRulesSet;
    }

    /**
     * Adds annotations to this nonterminal and sets them unmodifiable.
     *
     * @param newAnnotations annotations to add
     * @return whether this operation was successful
     */
    public boolean addAnnotations(List<GrammarNonterminalAnnotation> newAnnotations) {
        if (!areAnnotationsSet) {
            annotations.addAll(newAnnotations);
            areAnnotationsSet = true;
            return true;
        }
        return false;
    }

    public @Unmodifiable List<GrammarNonterminalAnnotation> getAnnotations() {
        return Collections.unmodifiableList(annotations);
    }

    public boolean hasAnnotation(@NotNull String value) {
        return annotations.contains(new GrammarNonterminalAnnotation(value));
    }

    public boolean areAnnotationsSet() {
        return areAnnotationsSet;
    }

    @Override
    public String toString() {
        return "%s -> %s %s"
                .formatted(
                        identifier,
                        String.join(
                                " | ",
                                productionRules.stream()
                                               .map(GrammarProduction::toString)
                                               .toList()),
                        String.join(
                                " ",
                                annotations.stream()
                                           .map(GrammarNonterminalAnnotation::toString)
                                           .toList()
                        )
                );
    }

    @Override
    public @NotNull String getFormatted() {
        return getIdentifier();
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    /**
     * Compares this GrammarNonterminal to a GrammarSymbol. If s is an instance of GrammarNonterminal their identifiers
     * are compared. False otherwise.
     *
     * @param s GrammarSymbol to compare to
     * @return if symbols match
     */
    public boolean symbolEquals(GrammarSymbol s) {
        if (s instanceof GrammarNonterminal g) {
            return getIdentifier().equals(g.getIdentifier());
        }
        return false;
    }

    @Override
    public boolean symbolDeepEquals(GrammarSymbol s) {
        if (s instanceof GrammarNonterminal other) {
            return getIdentifier().equals(other.getIdentifier()) && annotations.equals(other.annotations);
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrammarNonterminal g) {
            return getIdentifier().equals(g.getIdentifier()) && productionRules.equals(g.productionRules);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, productionRules);
    }
}
