

package com.android.inputmethod.compat;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

public final class InputMethodManagerCompatWrapper {
    // Note that InputMethodManager.switchToNextInputMethod() has been introduced
    // in API level 16 (Build.VERSION_CODES.JELLY_BEAN).
    private static final Method METHOD_switchToNextInputMethod = CompatUtils.getMethod(
            InputMethodManager.class, "switchToNextInputMethod", IBinder.class, boolean.class);

    // Note that InputMethodManager.shouldOfferSwitchingToNextInputMethod() has been introduced
    // in API level 19 (Build.VERSION_CODES.KITKAT).
    private static final Method METHOD_shouldOfferSwitchingToNextInputMethod =
            CompatUtils.getMethod(InputMethodManager.class,
                    "shouldOfferSwitchingToNextInputMethod", IBinder.class);

    public final InputMethodManager mImm;

    public InputMethodManagerCompatWrapper(final Context context) {
        mImm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public boolean switchToNextInputMethod(final IBinder token, final boolean onlyCurrentIme) {
        return (Boolean)CompatUtils.invoke(mImm, false /* defaultValue */,
                METHOD_switchToNextInputMethod, token, onlyCurrentIme);
    }

    public boolean shouldOfferSwitchingToNextInputMethod(final IBinder token) {
        return (Boolean)CompatUtils.invoke(mImm, false /* defaultValue */,
                METHOD_shouldOfferSwitchingToNextInputMethod, token);
    }
}
