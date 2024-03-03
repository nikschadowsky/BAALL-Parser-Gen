package de.nikschadowsky.baall.parsergen.generator.elements.definition;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 26.02.2024
 */
public class JavaSourceNamespaceDescriptor<NAMESPACE_TYPE> implements JavaSourceElement<JavaSourceNamespaceDescriptor<NAMESPACE_TYPE>> {

    public static final Map<Class<?>, String> PRIMITIVE_CLASS_NAMES = Map.of(
            Integer.class,
            "int",
            Double.class,
            "double",
            Float.class,
            "float",
            Long.class,
            "long",
            Character.class,
            "char",
            Byte.class,
            "byte"
    );

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
    public String getSourceCodeSnippet() {
        String typeName = PRIMITIVE_CLASS_NAMES.getOrDefault(type, type.getSimpleName());

        return JavaSourceElement.joinArgumentsString(accessLevel, modifierFlag) + typeName + name;
    public @NotNull String getSourceCodeSnippet() {
    }
}
