package de.nikschadowsky.baall.parsergen.generator.elements.operation;

import org.jetbrains.annotations.NotNull;

/**
 * File created on 02.03.2024
 */
public abstract class JavaSourcePrimitive<TYPE> implements JavaSourceValue<TYPE> {

    public static @NotNull JavaSourcePrimitive<Integer> fromInt(int value) {
        return new JavaSourceIntPrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<Short> fromShort(short value) {
        return new JavaSourceShortPrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<Byte> fromByte(byte value) {
        return new JavaSourceBytePrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<Character> fromChar(char value) {
        return new JavaSourceCharPrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<Long> fromLong(long value) {
        return new JavaSourceLongPrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<Double> fromDouble(double value) {
        return new JavaSourceDoublePrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<Float> fromFloat(float value) {
        return new JavaSourceFloatPrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<Boolean> fromBoolean(boolean value) {
        return new JavaSourceBooleanPrimitive(value);
    }

    public static @NotNull JavaSourcePrimitive<String> fromString(String value) {
        return new JavaSourceStringPrimitive(value);
    }

    private static class JavaSourceIntPrimitive extends JavaSourcePrimitive<Integer> {

        private final int value;

        public JavaSourceIntPrimitive(int value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Integer> getType() {
            return Integer.class;
        }
    }

    private static class JavaSourceShortPrimitive extends JavaSourcePrimitive<Short> {

        private final short value;

        public JavaSourceShortPrimitive(short value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Short> getType() {
            return Short.class;
        }
    }

    private static class JavaSourceBytePrimitive extends JavaSourcePrimitive<Byte> {

        private final byte value;

        public JavaSourceBytePrimitive(byte value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Byte> getType() {
            return Byte.class;
        }
    }

    private static class JavaSourceLongPrimitive extends JavaSourcePrimitive<Long> {

        private final long value;

        public JavaSourceLongPrimitive(long value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Long> getType() {
            return Long.class;
        }
    }

    private static class JavaSourceDoublePrimitive extends JavaSourcePrimitive<Double> {

        private final double value;

        public JavaSourceDoublePrimitive(double value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Double> getType() {
            return Double.class;
        }
    }

    private static class JavaSourceFloatPrimitive extends JavaSourcePrimitive<Float> {

        private final float value;

        public JavaSourceFloatPrimitive(float value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Float> getType() {
            return Float.class;
        }
    }

    private static class JavaSourceCharPrimitive extends JavaSourcePrimitive<Character> {

        private final char value;

        public JavaSourceCharPrimitive(char value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Character> getType() {
            return Character.class;
        }
    }

    private static class JavaSourceBooleanPrimitive extends JavaSourcePrimitive<Boolean> {

        private final boolean value;

        public JavaSourceBooleanPrimitive(boolean value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return String.valueOf(value);
        }

        @Override
        public @NotNull Class<Boolean> getType() {
            return Boolean.class;
        }
    }

    private static class JavaSourceStringPrimitive extends JavaSourcePrimitive<String> {

        private final String value;

        public JavaSourceStringPrimitive(String value) {
            this.value = value;
        }

        @Override
        public @NotNull String getSourceCodeSnippet() {
            return value;
        }

        @Override
        public @NotNull Class<String> getType() {
            return String.class;
        }
    }

}
