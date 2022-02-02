

package com.android.inputmethod.event;

import android.view.KeyEvent;

/**
 * An event decoder for hardware events.
 */
public interface HardwareEventDecoder extends EventDecoder {
    public Event decodeHardwareKey(final KeyEvent keyEvent);
}
