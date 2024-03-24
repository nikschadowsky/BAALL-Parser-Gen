package de.nikschadowsky.baall.parsergen.generator;

import com.squareup.javapoet.CodeBlock;
import de.nikschadowsky.baall.parsergen._utility.GrammarUtility;
import de.nikschadowsky.baall.parsergen.grammar.GrammarNonterminal;
import de.nikschadowsky.baall.parsergen.grammar.GrammarProduction;
import de.nikschadowsky.baall.parsergen.grammar.analysis.GrammarProductionTreeAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserClassGeneratorTest {

    @Test
    void generateTypeSpec() {
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void generateConditionCodeBlock(boolean hasEpsilon) {
        String expected = """
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
                """.formatted(hasEpsilon ? "return true;" : "// TODO add behaviour when this symbol couldn't be parsed;\nthrow new RuntimeException(\"Expected '?' but got '?'!\");");


        GrammarNonterminal nonterminal = new GrammarNonterminal("node");

        List<GrammarProduction> rules = new ArrayList<>();

        rules.add(new GrammarProduction(-1, GrammarUtility.getTerminalWithTypeAny("a")));
        rules.add(new GrammarProduction(-1, GrammarUtility.getTerminalWithTypeAny("a"), GrammarUtility.getTerminalWithTypeAny("b1")));
        rules.add(new GrammarProduction(-1, GrammarUtility.getTerminalWithTypeAny("a"), GrammarUtility.getTerminalWithTypeAny("b2"), GrammarUtility.getTerminalWithTypeAny("c1")));
        rules.add(new GrammarProduction(-1, nonterminal, GrammarUtility.getTerminalWithTypeAny("d")));

        if (hasEpsilon) {
            rules.add(new GrammarProduction(-1));
        }

        nonterminal.addProductionRules(rules);

        CodeBlock code = ParserClassGenerator.getInstance().generateConditionCodeBlock(GrammarProductionTreeAssembler.generateConditionTree(nonterminal));

        String actual = code.toString();
        System.out.println(actual);

        assertEquals(expected, actual);
    }
}