

package com.android.inputmethod.latin.makedict;

/**
 * Simple exception thrown when a file format is not recognized.
 */
public final class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String description) {
        super(description);
    }
}
