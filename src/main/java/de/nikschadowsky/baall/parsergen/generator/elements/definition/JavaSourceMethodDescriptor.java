package de.nikschadowsky.baall.parsergen.generator.elements.definition;

import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElement;
import de.nikschadowsky.baall.parsergen.generator.elements.JavaSourceElementList;
import org.jetbrains.annotations.NotNull;

/**
 * File created on 27.02.2024
 */
public class JavaSourceMethodDescriptor<RETURN_TYPE> implements JavaSourceElement<JavaSourceMethodDescriptor<RETURN_TYPE>> {

    private final JavaSourceNamespaceDescriptor<RETURN_TYPE> descriptor;
    private final JavaSourceElementList<JavaSourceParameterDescriptor<?>> parameters;
    private final JavaMethodBody<RETURN_TYPE> body;

    public JavaSourceMethodDescriptor(
            JavaSourceNamespaceDescriptor<RETURN_TYPE> descriptor,
            JavaSourceElementList<JavaSourceParameterDescriptor<?>> parameters,
            JavaMethodBody<RETURN_TYPE> body
    ) {
        this.descriptor = descriptor;
        this.parameters = parameters;
        this.body = body;
    }

    public JavaSourceElementList<JavaSourceParameterDescriptor<?>> getParameterDescriptors(){
        return parameterDescriptors;
    }

    @Override
    public String getSourceCodeSnippet() {
        return "%s(%s){%n          %s%n}".formatted(
                descriptor.getSourceCodeSnippet(),
                parameters.getSourceCodeSnippet(),
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
