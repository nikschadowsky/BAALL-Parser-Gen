package de.nikschadowsky.baall.parsergen.generator.elements.definition;

import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElement;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 26.02.2024
 */
public class JavaSourceNamespaceDescriptor<NAMESPACE_TYPE> implements JavaSourceElement<JavaSourceNamespaceDescriptor<NAMESPACE_TYPE>> {

    private final JavaSourceAccessLevel accessLevel;
    private final JavaSourceModifierFlag modifierFlag;
    private final Class<NAMESPACE_TYPE> type;
    private final String name;

    public JavaSourceNamespaceDescriptor(
            JavaSourceAccessLevel accessLevel,
            JavaSourceModifierFlag modifierFlag,
            Class<NAMESPACE_TYPE> type,
            String name
    ) {
        this.accessLevel = accessLevel;
        this.modifierFlag = modifierFlag;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Class<NAMESPACE_TYPE> getType() {
        return type;
    }

    @Override
    public @NotNull String getSourceCodeSnippet() {
        return String.join(
                " ",
                JavaSourceElement.joinArgumentsString(accessLevel, modifierFlag),
                JavaSourceElement.getSimpleTypeName(type),
                name
        );
    }
}
