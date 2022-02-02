
package com.android.inputmethod.compat;

import android.view.textservice.TextInfo;

import com.android.inputmethod.annotations.UsedForTesting;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

@UsedForTesting
public final class TextInfoCompatUtils {
    // Note that TextInfo.getCharSequence() is supposed to be available in API level 21 and later.
    private static final Method TEXT_INFO_GET_CHAR_SEQUENCE =
            CompatUtils.getMethod(TextInfo.class, "getCharSequence");
    private static final Constructor<?> TEXT_INFO_CONSTRUCTOR_FOR_CHAR_SEQUENCE =
            CompatUtils.getConstructor(TextInfo.class, CharSequence.class, int.class, int.class,
                    int.class, int.class);

    @UsedForTesting
    public static boolean isCharSequenceSupported() {
        return TEXT_INFO_GET_CHAR_SEQUENCE != null &&
                TEXT_INFO_CONSTRUCTOR_FOR_CHAR_SEQUENCE != null;
    }

    @UsedForTesting
    public static TextInfo newInstance(CharSequence charSequence, int start, int end, int cookie,
            int sequenceNumber) {
        if (TEXT_INFO_CONSTRUCTOR_FOR_CHAR_SEQUENCE != null) {
            return (TextInfo) CompatUtils.newInstance(TEXT_INFO_CONSTRUCTOR_FOR_CHAR_SEQUENCE,
                    charSequence, start, end, cookie, sequenceNumber);
        }
        return new TextInfo(charSequence.subSequence(start, end).toString(), cookie,
                sequenceNumber);
    }

    /**
     * Returns the result of {@link TextInfo#getCharSequence()} when available. Otherwise returns
     * the result of {@link TextInfo#getText()} as fall back.
     * @param textInfo the instance for which {@link TextInfo#getCharSequence()} or
     * {@link TextInfo#getText()} is called.
     * @return the result of {@link TextInfo#getCharSequence()} when available. Otherwise returns
     * the result of {@link TextInfo#getText()} as fall back. If {@code textInfo} is {@code null},
     * returns {@code null}.
     */
    @UsedForTesting
    public static CharSequence getCharSequenceOrString(final TextInfo textInfo) {
        final CharSequence defaultValue = (textInfo == null ? null : textInfo.getText());
        return (CharSequence) CompatUtils.invoke(textInfo, defaultValue,
                TEXT_INFO_GET_CHAR_SEQUENCE);
    }
}
