
package com.android.inputmethod.compat;

import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.view.View;

public class ViewOutlineProviderCompatUtils {
    private ViewOutlineProviderCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    public interface InsetsUpdater {
        public void setInsets(final InputMethodService.Insets insets);
    }

    private static final InsetsUpdater EMPTY_INSETS_UPDATER = new InsetsUpdater() {
        @Override
        public void setInsets(final InputMethodService.Insets insets) {}
    };

    public static InsetsUpdater setInsetsOutlineProvider(final View view) {
        if (BuildCompatUtils.EFFECTIVE_SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return EMPTY_INSETS_UPDATER;
        }
        return ViewOutlineProviderCompatUtilsLXX.setInsetsOutlineProvider(view);
    }
}
