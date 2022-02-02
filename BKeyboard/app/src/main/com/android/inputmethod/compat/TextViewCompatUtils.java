

package com.android.inputmethod.compat;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import java.lang.reflect.Method;

public final class TextViewCompatUtils {
    // Note that TextView.setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable,Drawable,
    // Drawable,Drawable) has been introduced in API level 17 (Build.VERSION_CODE.JELLY_BEAN_MR1).
    private static final Method METHOD_setCompoundDrawablesRelativeWithIntrinsicBounds =
            CompatUtils.getMethod(TextView.class, "setCompoundDrawablesRelativeWithIntrinsicBounds",
            Drawable.class, Drawable.class, Drawable.class, Drawable.class);

    private TextViewCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(final TextView textView,
            final Drawable start, final Drawable top, final Drawable end, final Drawable bottom) {
        if (METHOD_setCompoundDrawablesRelativeWithIntrinsicBounds == null) {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
            return;
        }
        CompatUtils.invoke(textView, null, METHOD_setCompoundDrawablesRelativeWithIntrinsicBounds,
                start, top, end, bottom);
    }
}
