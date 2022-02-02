
package com.android.inputmethod.latin.network;

/**
 * Authentication exception. When this exception is thrown, the client may
 * try to refresh the authentication token and try again.
 */
public class AuthException extends Exception {
    public AuthException() {
        super();
    }

    public AuthException(Throwable throwable) {
        super(throwable);
    }

    public AuthException(String detailMessage) {
        super(detailMessage);
    }
}