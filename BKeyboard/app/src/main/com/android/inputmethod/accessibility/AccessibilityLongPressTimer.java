
package com.android.inputmethod.accessibility;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.keyboard.Key;

// Handling long press timer to show a more keys keyboard.
final class AccessibilityLongPressTimer extends Handler {
    public interface LongPressTimerCallback {
        public void performLongClickOn(Key key);
    }

    private static final int MSG_LONG_PRESS = 1;

    private final LongPressTimerCallback mCallback;
    private final long mConfigAccessibilityLongPressTimeout;

    public AccessibilityLongPressTimer(final LongPressTimerCallback callback,
            final Context context) {
        super();
        mCallback = callback;
        mConfigAccessibilityLongPressTimeout = context.getResources().getInteger(
                R.integer.config_accessibility_long_press_key_timeout);
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
        case MSG_LONG_PRESS:
            cancelLongPress();
            mCallback.performLongClickOn((Key)msg.obj);
            return;
        default:
            super.handleMessage(msg);
            return;
        }
    }

    public void startLongPress(final Key key) {
        cancelLongPress();
        final Message longPressMessage = obtainMessage(MSG_LONG_PRESS, key);
        sendMessageDelayed(longPressMessage, mConfigAccessibilityLongPressTimeout);
    }

    public void cancelLongPress() {
        removeMessages(MSG_LONG_PRESS);
    }
}
