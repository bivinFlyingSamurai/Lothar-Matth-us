

package com.android.inputmethod.dictionarypack;

import android.content.ContentValues;

/**
 * Struct class to encapsulate a client ID with content values about a download.
 */
public class DownloadRecord {
    public final String mClientId;
    // Only word lists have attributes, and the ContentValues should contain the same
    // keys as they do for all MetadataDbHelper functions. Since only word lists have
    // attributes, a null pointer here means this record represents metadata.
    public final ContentValues mAttributes;
    public DownloadRecord(final String clientId, final ContentValues attributes) {
        mClientId = clientId;
        mAttributes = attributes;
    }
    public boolean isMetadata() {
        return null == mAttributes;
    }
}