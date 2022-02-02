

package com.android.inputmethod.dictionarypack;

/**
 * Exception thrown when the metadata for the dictionary does not comply to a known format.
 */
public final class BadFormatException extends Exception {
    public BadFormatException() {
        super();
    }

    public BadFormatException(final String message) {
        super(message);
    }
}
