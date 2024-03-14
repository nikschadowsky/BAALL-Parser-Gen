package de.nikschadowsky.baall.parsergen.grammar;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * File created on 24.02.2024
 */
public record GrammarTerminal(TerminalType type, String value) implements GrammarSymbol {

    @Override
    public @NotNull String getFormatted() {
        return value;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean symbolEquals(GrammarSymbol s) {
        if (s instanceof GrammarTerminal terminal) {

            // if one of the terminal is type ANY, compare literal value
            if (type().equals(TerminalType.ANY) || terminal.type().equals(TerminalType.ANY)) {
                return value.equals(terminal.value());
            }

            if (!type().hasExactValueMatching() && type().equals(terminal.type())) {
                return true;
            }

            // else the type must be equal too
            return type().equals(terminal.type()) && value().equals(terminal.value());

        }

        return false;
    }

    @Override
    public boolean symbolDeepEquals(GrammarSymbol s) {
        return equals(s);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrammarTerminal terminal)
            return type().equals(terminal.type()) && value().equals(terminal.value());
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type(), value());
    }

    public enum TerminalType {
        ANY("Any", true),
        STRING("String", false),
        NUMBER("Number", false),
        BOOLEAN("Boolean", false),
        IDENTIFIER("Identifier", false),
        KEYWORD("Keyword", true),
        OPERATOR("Operator", true),
        SEPARATOR("Separator", true);

        private final String description;

        private final boolean hasExactValueMatching;

        TerminalType(String description, boolean hasExactValueMatching) {
            this.description = description;
            this.hasExactValueMatching = hasExactValueMatching;
        }

        public static TerminalType getTypeFromDescription(String description) {
            return Arrays.stream(values())
                         .filter(value -> value.getDescription().equals(description))
                         .findAny()
                         .orElseThrow(() -> new GrammarSyntaxException.NoGrammarTerminalTypeException(
                                 "",
                                 "No type found with description '%s'".formatted(description)
                         ));
        }

        public String getDescription() {
            return description;
        }

        public boolean hasExactValueMatching() {
            return hasExactValueMatching;
        }
    }

}
