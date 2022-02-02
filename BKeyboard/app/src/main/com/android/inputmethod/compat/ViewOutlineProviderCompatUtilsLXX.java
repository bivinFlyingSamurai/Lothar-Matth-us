
package com.android.inputmethod.compat;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.android.inputmethod.compat.ViewOutlineProviderCompatUtils.InsetsUpdater;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ViewOutlineProviderCompatUtilsLXX {
    private ViewOutlineProviderCompatUtilsLXX() {
        // This utility class is not publicly instantiable.
    }

    static InsetsUpdater setInsetsOutlineProvider(final View view) {
        final InsetsOutlineProvider provider = new InsetsOutlineProvider(view);
        view.setOutlineProvider(provider);
        return provider;
    }

    private static class InsetsOutlineProvider extends ViewOutlineProvider
            implements InsetsUpdater {
        private final View mView;
        private static final int NO_DATA = -1;
        private int mLastVisibleTopInsets = NO_DATA;

        public InsetsOutlineProvider(final View view) {
            mView = view;
            view.setOutlineProvider(this);
        }

        @Override
        public void setInsets(final InputMethodService.Insets insets) {
            final int visibleTopInsets = insets.visibleTopInsets;
            if (mLastVisibleTopInsets != visibleTopInsets) {
                mLastVisibleTopInsets = visibleTopInsets;
                mView.invalidateOutline();
            }
        }

        @Override
        public void getOutline(final View view, final Outline outline) {
            if (mLastVisibleTopInsets == NO_DATA) {
                // Call default implementation.
                ViewOutlineProvider.BACKGROUND.getOutline(view, outline);
                return;
            }
            // TODO: Revisit this when floating/resize keyboard is supported.
            outline.setRect(
                    view.getLeft(), mLastVisibleTopInsets, view.getRight(), view.getBottom());
        }
    }
}
