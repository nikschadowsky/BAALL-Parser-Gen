package de.nikschadowsky.baall.parsergen.generator.elements.definition;

import org.jetbrains.annotations.NotNull;

/**
 * File created on 02.03.2024
 */
public class JavaSourceFieldDescriptor<FIELD_TYPE> extends JavaSourceNamespaceDescriptor<FIELD_TYPE> {

    public JavaSourceFieldDescriptor(
            JavaSourceAccessLevel accessLevel,
            JavaSourceModifierFlag modifierFlag,
            Class<FIELD_TYPE> type,
            String fieldName
    ) {
        super(accessLevel, modifierFlag, type, fieldName);
    }
}
