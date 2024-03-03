package de.nikschadowsky.baall.parsergen.generator.elements.definition;

import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElement;
import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElementList;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 27.02.2024
 */
public class JavaSourceMethodDescriptor<RETURN_TYPE> extends JavaSourceNamespaceDescriptor<RETURN_TYPE> {

    private final JavaSourceElementList<JavaSourceParameterDescriptor<?>> parameterDescriptors;
    private final JavaMethodBody<RETURN_TYPE> body;

    public JavaSourceMethodDescriptor(
            JavaSourceAccessLevel accessLevel,
            JavaSourceModifierFlag modifierFlag,
            Class<RETURN_TYPE> type,
            String methodName,
            JavaSourceElementList<JavaSourceParameterDescriptor<?>> parameterDescriptors,
            JavaMethodBody<RETURN_TYPE> body
    ) {
        super(accessLevel, modifierFlag, type, methodName);
        this.parameterDescriptors = parameterDescriptors;
        this.body = body;
    }

    public JavaSourceElementList<JavaSourceParameterDescriptor<?>> getParameterDescriptors(){
        return parameterDescriptors;
    }

    @Override
    public @NotNull String getSourceCodeSnippet() {
        return "%s(%s){%n%s%n}".formatted(
                super.getSourceCodeSnippet(),
                parameterDescriptors.getSourceCodeSnippet(),
                body.getSourceCodeSnippet()
        );
    }

    public static class JavaMethodBody<RETURN_TYPE> implements JavaSourceElement<RETURN_TYPE> {

        private final String body;

        public JavaMethodBody(String body) {
            this.body = body;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return body;
        }
    }

}
