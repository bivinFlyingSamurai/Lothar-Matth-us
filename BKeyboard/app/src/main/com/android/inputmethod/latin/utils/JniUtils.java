

package com.android.inputmethod.latin.utils;

import android.util.Log;

import com.android.inputmethod.latin.define.JniLibName;

public final class JniUtils {
    private static final String TAG = JniUtils.class.getSimpleName();

    static {
        try {
            System.loadLibrary(JniLibName.JNI_LIB_NAME2);
        } catch (Exception ue) {
            Log.e(TAG, "Could not load native library " + JniLibName.JNI_LIB_NAME2, ue);
            try {
                System.loadLibrary(JniLibName.JNI_LIB_NAME);
            } catch (Exception ule) {
                Log.e(TAG, "Could not load native library " + JniLibName.JNI_LIB_NAME, ule);
            }
        }
    }
    private JniUtils() {
        // This utility class is not publicly instantiable.
    }

    public static void loadNativeLibrary() {
        // Ensures the static initializer is called
    }
}
