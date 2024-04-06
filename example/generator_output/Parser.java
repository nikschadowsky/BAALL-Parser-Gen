package x.y.z;

import java.util.Map;
import java.util.Queue;

public class Parser {
  private static final Map<String, TerminalSymbol> TERMINAL_MAP = generateMapEntries();

  private Queue<TerminalComparable> queue;

  public Parser(Queue<TerminalComparable> tokens) {
    this.queue = tokens;
  }

  private static Map<String, TerminalSymbol> generateMapEntries() {
    return Map.ofEntries(
        Map.entry("(", new TerminalSymbol(TerminalType.ANY, "(")),
        Map.entry(")", new TerminalSymbol(TerminalType.ANY, ")")),
        Map.entry("", new TerminalSymbol(TerminalType.NUMBER, "")),
        Map.entry("+", new TerminalSymbol(TerminalType.ANY, "+")),
        Map.entry("-", new TerminalSymbol(TerminalType.ANY, "-")),
        Map.entry("*", new TerminalSymbol(TerminalType.ANY, "*")),
        Map.entry("/", new TerminalSymbol(TerminalType.ANY, "/"))
        );
  }

  private boolean parseStart(Queue<TerminalComparable> queue) {
    if (parseExpr(queue)) {
      return true;
    }
    // TODO add behaviour when this symbol couldn't be parsed
    throw new RuntimeException("Expected '?' but got '?'!");
  }

  private boolean parseExpr(Queue<TerminalComparable> queue) {
    if (TERMINAL_MAP.get("(").symbolMatches(queue.poll())) {
      if (parseExpr(queue)) {
        if (TERMINAL_MAP.get(")").symbolMatches(queue.poll())) {
          return true;
        }
      }
    }
    else if (parseTerm(queue)) {
      if (parseOp(queue)) {
        if (parseExpr(queue)) {
          return true;
        }
      }
      return true;
    }
    // TODO add behaviour when this symbol couldn't be parsed
    throw new RuntimeException("Expected '?' but got '?'!");
  }

  private boolean parseTerm(Queue<TerminalComparable> queue) {
    if (TERMINAL_MAP.get("").symbolMatches(queue.poll())) {
      return true;
    }
    // TODO add behaviour when this symbol couldn't be parsed
    throw new RuntimeException("Expected '?' but got '?'!");
  }

  private boolean parseOp(Queue<TerminalComparable> queue) {
    if (TERMINAL_MAP.get("+").symbolMatches(queue.poll())) {
      return true;
    }
    else if (TERMINAL_MAP.get("-").symbolMatches(queue.poll())) {
      return true;
    }
    else if (TERMINAL_MAP.get("*").symbolMatches(queue.poll())) {
      return true;
    }
    else if (TERMINAL_MAP.get("/").symbolMatches(queue.poll())) {
      return true;
    }
    // TODO add behaviour when this symbol couldn't be parsed
    throw new RuntimeException("Expected '?' but got '?'!");
  }

  private static class TerminalSymbol implements TerminalComparable {
    private final String value;

    private final TerminalType type;

    public TerminalSymbol(TerminalType type, String value) {
      this.type = type;
      this.value = value;
    }

    public boolean symbolMatches(TerminalComparable symbol) {
      if (!TerminalType.ANY.equals(type)) {
        return value.equals(symbol.getValue());
      } if (type.equals(symbol.getType())) {
        return !type.hasExactValueMatching() || value.equals(symbol.getType());
      }
      return false;
    }

    @Override
    public TerminalType getType() {
      return type;
    }

    @Override
    public String getValue() {
      return value;
    }
  }
}
