package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.MethodSpec;
import de.nikschadowsky.baall.parsergen._utility.GrammarUtility;
import de.nikschadowsky.baall.parsergen.grammar.analysis.GrammarTerminalUniqueList;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import de.nikschadowsky.baall.parsergen.grammar.GrammarTerminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ParserClassGeneratorTest {

    @Test
    void generateTypeSpec() {
        fail();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void generateConditionCodeBlock(boolean hasEpsilon) {
        String expected = """
                private boolean parseNode(java.util.Queue<TerminalComparable> queue) {
                  if (TERMINAL_MAP.get("a").symbolMatches(queue.poll())) {
                    if (TERMINAL_MAP.get("b1").symbolMatches(queue.poll())) {
                      return true;
                    }
                    else if (TERMINAL_MAP.get("b2").symbolMatches(queue.poll())) {
                      if (TERMINAL_MAP.get("c1").symbolMatches(queue.poll())) {
                        return true;
                      }
                    }
                    return true;
                  }
                  else if (parseNode(queue)) {
                    if (TERMINAL_MAP.get("d").symbolMatches(queue.poll())) {
                      return true;
                    }
                  }
                  %s
                }
                """.formatted(hasEpsilon ? "return true;" : "// TODO add behaviour when this symbol couldn't be parsed\n" + "  throw new RuntimeException(\"Expected '?' but got '?'!\");");


        GrammarNonterminal nonterminal = new GrammarNonterminal("node");

        List<GrammarProduction> rules = new ArrayList<>();

        rules.add(new GrammarProduction(-1, GrammarUtility.getTerminalWithTypeAny("a")));
        rules.add(new GrammarProduction(
                -1,
                GrammarUtility.getTerminalWithTypeAny("a"),
                GrammarUtility.getTerminalWithTypeAny("b1")
        ));
        rules.add(new GrammarProduction(
                -1,
                GrammarUtility.getTerminalWithTypeAny("a"),
                GrammarUtility.getTerminalWithTypeAny("b2"),
                GrammarUtility.getTerminalWithTypeAny("c1")
        ));
        rules.add(new GrammarProduction(-1, nonterminal, GrammarUtility.getTerminalWithTypeAny("d")));

        if (hasEpsilon) {
            rules.add(new GrammarProduction(-1));
        }

        nonterminal.addProductionRules(rules);

        MethodSpec method = ParserClassGenerator.getInstance().generateParserMethodForNonterminal(nonterminal);

        String actual = method.toString();
        System.out.println(actual);

        assertEquals(expected, actual);
    }


    @ParameterizedTest
    @MethodSource(value = "mapEntryDataProvider")
    void generateMapEntryMethod(GrammarTerminalUniqueList terminals, String expectedEntryStrings) {
        String expected = """
                private static java.util.Map<java.lang.String, TerminalSymbol> generateMapEntries() {
                  return java.util.Map.ofEntries(%s);
                }
                """.formatted(expectedEntryStrings);


        String result = ParserClassGenerator.getInstance().generateMapEntryMethod(terminals).toString();
        System.out.println(result);
        assertEquals(expected, result);
    }

    static Stream<Arguments> mapEntryDataProvider() {
        GrammarTerminalUniqueList nonEmpty = new GrammarTerminalUniqueList();
        nonEmpty.add(new GrammarTerminal(GrammarTerminal.TerminalType.ANY, "any"));
        nonEmpty.add(new GrammarTerminal(GrammarTerminal.TerminalType.NUMBER, "number"));
        nonEmpty.add(new GrammarTerminal(GrammarTerminal.TerminalType.KEYWORD, "keyword"));

        return Stream.of(Arguments.of(new GrammarTerminalUniqueList(), ""), Arguments.of(
                nonEmpty,
                """
                                                          
                              Map.entry("any", new TerminalSymbol(TerminalType.ANY, "any")),
                              Map.entry("number", new TerminalSymbol(TerminalType.NUMBER, "number")),
                              Map.entry("keyword", new TerminalSymbol(TerminalType.KEYWORD, "keyword"))
                        \s\s\s\s\s\s"""
        ));
    }

    @Test
    void generateConstructor() {
        // the constructor is called 'Constructor' here because the generator was not called by a type spec
        String expected = """
                public Constructor(java.util.Queue<TerminalComparable> tokens) {
                  this.queue = tokens;
                }
                """;

        assertEquals(expected, ParserClassGenerator.getInstance().generateConstructor().toString());
    }
}