package de.nikschadowsky.baall.parsergen.generator;

import de.nikschadowsky.baall.parsergen.generator.elements.*;
import de.nikschadowsky.baall.parsergen.util.FileUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * File created on 26.02.2024
 */
public abstract class JavaSourceGenerator {

    public static final String SINGLE_INDENT = "     ";

    private String template;

    private String className;

    private final JavaSourceElementList<JavaSourceNamespaceDescriptor<?>> fields =
            new JavaSourceElementList<>("class_fields", ("%s;%n" + SINGLE_INDENT + "%s")::formatted);
    private final JavaSourceElementList<JavaSourceParameterDescriptor<?>> constructorParameters =
            new JavaSourceElementList<>("const_parameters", "%s %s"::formatted);

    private final JavaSourceElementList<JavaSourceMethodDescriptor<?>> methods =
            new JavaSourceElementList<>("methods", "%s%n%n%s"::formatted);

    private final List<String> toBeInit = new ArrayList<>();

    public JavaSourceGenerator(String templatePath) {
        this.template = FileUtility.getFileContentFromClasspath(templatePath);
    }

    public JavaSourceGenerator setClassName(String className) {
        this.className = className;
        return this;
    }

    public <T> JavaSourceGenerator addFinalField(Class<T> type, String fieldName) {
        fields.add(new JavaSourceNamespaceDescriptor<>(
                JavaSourceAccessLevel.PRIVATE,
                JavaSourceModifierFlag.FINAL,
                type,
                fieldName
        ));
        constructorParameters.add(new JavaSourceParameterDescriptor<T>(
                type,
                fieldName
        ));
        return this;
    }

    public JavaSourceGenerator addField(JavaSourceNamespaceDescriptor<?> field) {
        fields.add(field);
        return this;
    }

    public JavaSourceGenerator addMethod(JavaSourceMethodDescriptor<?> method) {
        methods.add(method);
        return this;
    }

    public void generate() {
        replacePlacementId("package_id", "your.package");
        replacePlacementId("class_name", className);

        replacePlacementId(fields.getPlaceholderId(), fields.getSourceCodeSnippet());
        replacePlacementId(constructorParameters.getPlaceholderId(), constructorParameters.getSourceCodeSnippet());
        replacePlacementId("", generateInitializations(constructorParameters));
    }

    private String generateInitializations(JavaSourceElementList<JavaSourceParameterDescriptor<?>> parameters) {
        List<String> initialized = parameters.stream()
                                             .map(JavaSourceParameterDescriptor::getName)
                                             .map("this.%s = %s;"::formatted)
                                             .toList();

        return String.join("\n          ", initialized);
    }

    protected void replacePlacementId(String placeholderId, String replacement) {
        template = template.replaceAll(placeholderId, replacement);
    }

}
