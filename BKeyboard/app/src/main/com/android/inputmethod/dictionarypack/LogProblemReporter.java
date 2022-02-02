

package com.android.inputmethod.dictionarypack;

import android.util.Log;

/**
 * A very simple problem reporter.
 */
final class LogProblemReporter implements ProblemReporter {
    private final String TAG;

    public LogProblemReporter(final String tag) {
        TAG = tag;
    }

    @Override
    public void report(final Exception e) {
        Log.e(TAG, "Reporting problem", e);
    }
}
