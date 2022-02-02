
package com.android.inputmethod.event;

import com.android.inputmethod.latin.settings.SettingsValues;

/**
 * An object encapsulating a single transaction for input.
 */
public class InputTransaction {
    // UPDATE_LATER is stronger than UPDATE_NOW. The reason for this is, if we have to update later,
    // it's because something will change that we can't evaluate now, which means that even if we
    // re-evaluate now we'll have to do it again later. The only case where that wouldn't apply
    // would be if we needed to update now to find out the new state right away, but then we
    // can't do it with this deferred mechanism anyway.
    public static final int SHIFT_NO_UPDATE = 0;
    public static final int SHIFT_UPDATE_NOW = 1;
    public static final int SHIFT_UPDATE_LATER = 2;

    // Initial conditions
    public final SettingsValues mSettingsValues;
    public final Event mEvent;
    public final long mTimestamp;
    public final int mSpaceState;
    public final int mShiftState;

    // Outputs
    private int mRequiredShiftUpdate = SHIFT_NO_UPDATE;
    private boolean mRequiresUpdateSuggestions = false;
    private boolean mDidAffectContents = false;
    private boolean mDidAutoCorrect = false;

    public InputTransaction(final SettingsValues settingsValues, final Event event,
            final long timestamp, final int spaceState, final int shiftState) {
        mSettingsValues = settingsValues;
        mEvent = event;
        mTimestamp = timestamp;
        mSpaceState = spaceState;
        mShiftState = shiftState;
    }

    /**
     * Indicate that this transaction requires some type of shift update.
     * @param updateType What type of shift update this requires.
     */
    public void requireShiftUpdate(final int updateType) {
        mRequiredShiftUpdate = Math.max(mRequiredShiftUpdate, updateType);
    }

    /**
     * Gets what type of shift update this transaction requires.
     * @return The shift update type.
     */
    public int getRequiredShiftUpdate() {
        return mRequiredShiftUpdate;
    }

    /**
     * Indicate that this transaction requires updating the suggestions.
     */
    public void setRequiresUpdateSuggestions() {
        mRequiresUpdateSuggestions = true;
    }

    /**
     * Find out whether this transaction requires updating the suggestions.
     * @return Whether this transaction requires updating the suggestions.
     */
    public boolean requiresUpdateSuggestions() {
        return mRequiresUpdateSuggestions;
    }

    /**
     * Indicate that this transaction affected the contents of the editor.
     */
    public void setDidAffectContents() {
        mDidAffectContents = true;
    }

    /**
     * Find out whether this transaction affected contents of the editor.
     * @return Whether this transaction affected contents of the editor.
     */
    public boolean didAffectContents() {
        return mDidAffectContents;
    }

    /**
     * Indicate that this transaction performed an auto-correction.
     */
    public void setDidAutoCorrect() {
        mDidAutoCorrect = true;
    }

    /**
     * Find out whether this transaction performed an auto-correction.
     * @return Whether this transaction performed an auto-correction.
     */
    public boolean didAutoCorrect() {
        return mDidAutoCorrect;
    }
}
