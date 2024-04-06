package de.nikschadowsky.baall.parsergen.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * File created on 14.01.2024
 */
public class ArrayUtility {

    public static <T> T[] subarray(@NotNull T[] array, int lowerIndex, int upperIndex) {
        if (upperIndex > array.length) {
            throw new IllegalArgumentException("Upper index may not lie outside array!");
        }
        if (upperIndex == -1) {
            return Arrays.copyOfRange(array, lowerIndex, array.length);
        }

        return Arrays.copyOfRange(array, lowerIndex, upperIndex);
    }

    public static int[] range(int startInclusive, int endExclusive) {
        return IntStream.range(startInclusive, endExclusive).toArray();
    }

    public static int[] rangeClosed(int startInclusive, int endInclusive) {
        return range(startInclusive, endInclusive + 1);
    }

}
