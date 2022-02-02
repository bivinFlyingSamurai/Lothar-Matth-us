
package com.android.inputmethod.latin.network;

import com.android.inputmethod.annotations.UsedForTesting;

/**
 * The HttpException exception represents a XML/HTTP fault with a HTTP status code.
 */
public class HttpException extends Exception {

    /**
     * The HTTP status code.
     */
    private final int mStatusCode;

    /**
     * @param statusCode int HTTP status code.
     */
    public HttpException(int statusCode) {
        super("Response Code: " + statusCode);
        mStatusCode = statusCode;
    }

    /**
     * @return the HTTP status code related to this exception.
     */
    @UsedForTesting
    public int getHttpStatusCode() {
        return mStatusCode;
    }
}