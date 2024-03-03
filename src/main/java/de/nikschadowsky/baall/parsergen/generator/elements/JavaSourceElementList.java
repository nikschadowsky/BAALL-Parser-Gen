package de.nikschadowsky.baall.parsergen.generator.elements;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * File created on 27.02.2024
 */
public class JavaSourceElementList<ELEMENT_TYPE extends JavaSourceElement<?>> extends ArrayList<ELEMENT_TYPE> implements JavaSourceElement<JavaSourceElementList<ELEMENT_TYPE>> {

    private final String placeholderId;
    private final BinaryOperator<String> sourceCodeFunction;

    public JavaSourceElementList(String placeholderId, BinaryOperator<String> sourceCodeFunction) {
        this.placeholderId = placeholderId;
        this.sourceCodeFunction = sourceCodeFunction;
    }

    @Override
    public @NotNull String getSourceCodeSnippet() {
        return stream().map(JavaSourceElement::getSourceCodeSnippet).reduce(
                "", sourceCodeFunction
        );
    }

    public String getPlaceholderId() {
        return placeholderId;
    }

    public JavaSourceElementList<ELEMENT_TYPE> toUnmodifiableList(){
        return new JavaSourceUnmodifiableElementList<>(this);
    }

    private static class JavaSourceUnmodifiableElementList<ELEMENT_TYPE extends JavaSourceElement<?>> extends JavaSourceElementList<ELEMENT_TYPE> {

        public JavaSourceUnmodifiableElementList(@NotNull JavaSourceElementList<ELEMENT_TYPE> list) {
            super(list.placeholderId, list.sourceCodeFunction);
        }

        @Override
        public boolean add(ELEMENT_TYPE elementType) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public void add(int index, ELEMENT_TYPE element) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public boolean addAll(Collection<? extends ELEMENT_TYPE> c) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public boolean addAll(int index, Collection<? extends ELEMENT_TYPE> c) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public boolean removeIf(Predicate<? super ELEMENT_TYPE> filter) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public ELEMENT_TYPE remove(int index) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public void replaceAll(UnaryOperator<ELEMENT_TYPE> operator) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }

        @Override
        public ELEMENT_TYPE set(int index, ELEMENT_TYPE element) {
            throw new UnsupportedOperationException("List is unmodifiable!");
        }
    }
}
