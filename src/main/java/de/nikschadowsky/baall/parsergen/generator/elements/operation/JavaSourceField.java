package de.nikschadowsky.baall.parsergen.generator.elements.operation;

import de.nikschadowsky.baall.parsergen.generator.elements.definition.JavaSourceFieldDescriptor;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 29.02.2024
 */
public class JavaSourceField<FIELD_TYPE> implements JavaSourceValue<FIELD_TYPE> {

    private final JavaSourceFieldDescriptor<FIELD_TYPE> field;
    private final boolean useThis;

    public JavaSourceField(JavaSourceFieldDescriptor<FIELD_TYPE> field) {
        this(field, false);
    }

    public JavaSourceField(JavaSourceFieldDescriptor<FIELD_TYPE> field, boolean useThis) {
        this.field = field;
        this.useThis = useThis;
    }

    @Override
    public @NotNull String getSourceCodeSnippet() {
        return (useThis ? "this." : "") + field.getName();
    }

    @Override
    public @NotNull Class<FIELD_TYPE> getType() {
        return field.getType();
    }
}
