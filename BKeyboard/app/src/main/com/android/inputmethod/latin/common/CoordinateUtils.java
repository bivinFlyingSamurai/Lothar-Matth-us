

package com.android.inputmethod.latin.common;


public final class CoordinateUtils {
    private static final int INDEX_X = 0;
    private static final int INDEX_Y = 1;
    private static final int ELEMENT_SIZE = INDEX_Y + 1;

    private CoordinateUtils() {
        // This utility class is not publicly instantiable.
    }

    public static int[] newInstance() {
        return new int[ELEMENT_SIZE];
    }

    public static int x(final int[] coords) {
        return coords[INDEX_X];
    }

    public static int y(final int[] coords) {
        return coords[INDEX_Y];
    }

    public static void set(final int[] coords, final int x, final int y) {
        coords[INDEX_X] = x;
        coords[INDEX_Y] = y;
    }

    public static void copy(final int[] destination, final int[] source) {
        destination[INDEX_X] = source[INDEX_X];
        destination[INDEX_Y] = source[INDEX_Y];
    }

    public static int[] newCoordinateArray(final int arraySize) {
        return new int[ELEMENT_SIZE * arraySize];
    }

    public static int[] newCoordinateArray(final int arraySize,
                                           final int defaultX, final int defaultY) {
        final int[] result = new int[ELEMENT_SIZE * arraySize];
        for (int i = 0; i < arraySize; ++i) {
            setXYInArray(result, i, defaultX, defaultY);
        }
        return result;
    }

    public static int xFromArray(final int[] coordsArray, final int index) {
        return coordsArray[ELEMENT_SIZE * index + INDEX_X];
    }

    public static int yFromArray(final int[] coordsArray, final int index) {
        return coordsArray[ELEMENT_SIZE * index + INDEX_Y];
    }

    public static int[] coordinateFromArray(final int[] coordsArray, final int index) {
        final int[] coords = newInstance();
        set(coords, xFromArray(coordsArray, index), yFromArray(coordsArray, index));
        return coords;
    }

    public static void setXYInArray(final int[] coordsArray, final int index,
                                    final int x, final int y) {
        final int baseIndex = ELEMENT_SIZE * index;
        coordsArray[baseIndex + INDEX_X] = x;
        coordsArray[baseIndex + INDEX_Y] = y;
    }

    public static void setCoordinateInArray(final int[] coordsArray, final int index,
                                            final int[] coords) {
        setXYInArray(coordsArray, index, x(coords), y(coords));
    }
}
