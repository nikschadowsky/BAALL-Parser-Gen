package de.nikschadowsky.baall.parsergen.grammar;

public class GrammarSyntaxException extends RuntimeException {

    public GrammarSyntaxException(String additionalInformation, String reason) {
        super("Invalid grammar syntax! %s%nReason: %s".formatted(additionalInformation, reason));
    }

    public GrammarSyntaxException(String reason) {
        this("", reason);
    }

    public GrammarSyntaxException(String additionalInformation, String reason, Throwable cause) {
        super("Invalid grammar syntax! %s%nReason: %s".formatted(additionalInformation, reason), cause);
    }

    public static class NoGrammarTerminalTypeException extends GrammarSyntaxException{

        public NoGrammarTerminalTypeException(String additionalInformation, String reason) {
            super(additionalInformation, reason);
        }
    }
}
