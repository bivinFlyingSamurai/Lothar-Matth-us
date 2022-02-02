

package com.android.inputmethod.compat;

import android.net.ConnectivityManager;

import java.lang.reflect.Method;

public final class ConnectivityManagerCompatUtils {
    // ConnectivityManager#isActiveNetworkMetered() has been introduced
    // in API level 16 (Build.VERSION_CODES.JELLY_BEAN).
    private static final Method METHOD_isActiveNetworkMetered = CompatUtils.getMethod(
            ConnectivityManager.class, "isActiveNetworkMetered");

    public static boolean isActiveNetworkMetered(final ConnectivityManager manager) {
        return (Boolean)CompatUtils.invoke(manager,
                // If the API telling whether the network is metered or not is not available,
                // then the closest thing is "if it's a mobile connection".
                manager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE,
                METHOD_isActiveNetworkMetered);
    }
}
