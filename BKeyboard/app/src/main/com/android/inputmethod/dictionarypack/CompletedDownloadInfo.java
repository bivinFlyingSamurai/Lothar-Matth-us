

package com.android.inputmethod.dictionarypack;

import android.app.DownloadManager;

/**
 * Struct class to encapsulate the result of a completed download.
 */
public class CompletedDownloadInfo {
    final String mUri;
    final long mDownloadId;
    final int mStatus;
    public CompletedDownloadInfo(final String uri, final long downloadId, final int status) {
        mUri = uri;
        mDownloadId = downloadId;
        mStatus = status;
    }
    public boolean wasSuccessful() {
        return DownloadManager.STATUS_SUCCESSFUL == mStatus;
    }
}
