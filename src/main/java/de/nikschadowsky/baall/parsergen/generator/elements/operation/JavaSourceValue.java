package de.nikschadowsky.baall.parsergen.generator.elements.operation;

import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElement;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 29.02.2024
 */
public interface JavaSourceValue<VALUE_TYPE> extends JavaSourceElement<JavaSourceValue<VALUE_TYPE>> {

    @NotNull Class<VALUE_TYPE> getType();

}