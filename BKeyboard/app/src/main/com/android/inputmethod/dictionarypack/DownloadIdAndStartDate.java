
package com.android.inputmethod.dictionarypack;

/**
 * A simple container of download ID and download start date.
 */
public class DownloadIdAndStartDate {
    public final long mId;
    public final long mStartDate;
    public DownloadIdAndStartDate(final long id, final long startDate) {
        mId = id;
        mStartDate = startDate;
    }
}
