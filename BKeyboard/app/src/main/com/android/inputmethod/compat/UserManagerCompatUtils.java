

package com.android.inputmethod.compat;

import android.content.Context;
import android.os.Build;
import android.os.UserManager;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.reflect.Method;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * A temporary solution until {@code UserManagerCompat.isUserUnlocked()} in the support-v4 library
 * becomes publicly available.
 */
public final class UserManagerCompatUtils {
    private static final Method METHOD_isUserUnlocked;

    static {
        // We do not try to search the method in Android M and prior.
        if (BuildCompatUtils.EFFECTIVE_SDK_INT <= Build.VERSION_CODES.M) {
            METHOD_isUserUnlocked = null;
        } else {
            METHOD_isUserUnlocked = CompatUtils.getMethod(UserManager.class, "isUserUnlocked");
        }
    }

    private UserManagerCompatUtils() {
        // This utility class is not publicly instantiable.
    }

    public static final int LOCK_STATE_UNKNOWN = 0;
    public static final int LOCK_STATE_UNLOCKED = 1;
    public static final int LOCK_STATE_LOCKED = 2;

    @Retention(SOURCE)
    @IntDef({LOCK_STATE_UNKNOWN, LOCK_STATE_UNLOCKED, LOCK_STATE_LOCKED})
    public @interface LockState {}

    /**
     * Check if the calling user is running in an "unlocked" state. A user is unlocked only after
     * they've entered their credentials (such as a lock pattern or PIN), and credential-encrypted
     * private app data storage is available.
     * @param context context from which {@link UserManager} should be obtained.
     * @return One of {@link LockState}.
     */
    @LockState
    public static int getUserLockState(final Context context) {
        if (METHOD_isUserUnlocked == null) {
            return LOCK_STATE_UNKNOWN;
        }
        final UserManager userManager = context.getSystemService(UserManager.class);
        if (userManager == null) {
            return LOCK_STATE_UNKNOWN;
        }
        final Boolean result =
                (Boolean) CompatUtils.invoke(userManager, null, METHOD_isUserUnlocked);
        if (result == null) {
            return LOCK_STATE_UNKNOWN;
        }
        return result ? LOCK_STATE_UNLOCKED : LOCK_STATE_LOCKED;
    }
}
