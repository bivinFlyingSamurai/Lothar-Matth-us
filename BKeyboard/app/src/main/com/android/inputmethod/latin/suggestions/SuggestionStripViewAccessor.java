
package com.android.inputmethod.latin.suggestions;

import com.android.inputmethod.latin.SuggestedWords;

/**
 * An object that gives basic control of a suggestion strip and some info on it.
 */
public interface SuggestionStripViewAccessor {
    public void setNeutralSuggestionStrip();
    public void showSuggestionStrip(final SuggestedWords suggestedWords);
}
