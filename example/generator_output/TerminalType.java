package x.y.z;

import javax.annotation.processing.Generated;

@Generated("by BAALL-Parser-Gen")
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

  public String getDescription() {
    return description;
  }

  public boolean hasExactValueMatching() {
    return hasExactValueMatching;
  }
}
