package de.nikschadowsky.baall.parsergen.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

/**
 * File created on 27.03.2024
 */
public class ImmutableLinkedHashSet<T> extends LinkedHashSet<T> {

    public static <T> @NotNull LinkedHashSet<T> createFrom(LinkedHashSet<T> original) {
        return new ImmutableLinkedHashSet<>(original);
    }

    private ImmutableLinkedHashSet(@NotNull Collection<? extends T> c) {
        // do not replace this. implementation calls overridden add().
        //noinspection UseBulkOperation
        c.forEach(super::add);
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        throw new UnsupportedOperationException();
    }
}
