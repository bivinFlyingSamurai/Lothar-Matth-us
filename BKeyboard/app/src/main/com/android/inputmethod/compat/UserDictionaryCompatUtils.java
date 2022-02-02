

package com.android.inputmethod.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.UserDictionary;

import java.util.Locale;

public final class UserDictionaryCompatUtils {
    @SuppressWarnings("deprecation")
    public static void addWord(final Context context, final String word,
            final int freq, final String shortcut, final Locale locale) {
        if (BuildCompatUtils.EFFECTIVE_SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            addWordWithShortcut(context, word, freq, shortcut, locale);
            return;
        }
        // Fall back to the pre-JellyBean method.
        final Locale currentLocale = context.getResources().getConfiguration().locale;
        final int localeType = currentLocale.equals(locale)
                ? UserDictionary.Words.LOCALE_TYPE_CURRENT : UserDictionary.Words.LOCALE_TYPE_ALL;
        UserDictionary.Words.addWord(context, word, freq, localeType);
    }

    // {@link UserDictionary.Words#addWord(Context,String,int,String,Locale)} was introduced
    // in API level 16 (Build.VERSION_CODES.JELLY_BEAN).
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void addWordWithShortcut(final Context context, final String word,
            final int freq, final String shortcut, final Locale locale) {
        UserDictionary.Words.addWord(context, word, freq, shortcut, locale);
    }
}

