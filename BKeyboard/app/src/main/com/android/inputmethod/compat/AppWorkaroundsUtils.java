

package com.android.inputmethod.compat;

import android.content.pm.PackageInfo;
import android.os.Build.VERSION_CODES;

/**
 * A class to encapsulate work-arounds specific to particular apps.
 */
public class AppWorkaroundsUtils {
    private final PackageInfo mPackageInfo; // May be null
    private final boolean mIsBrokenByRecorrection;

    public AppWorkaroundsUtils(final PackageInfo packageInfo) {
        mPackageInfo = packageInfo;
        mIsBrokenByRecorrection = AppWorkaroundsHelper.evaluateIsBrokenByRecorrection(
                packageInfo);
    }

    public boolean isBrokenByRecorrection() {
        return mIsBrokenByRecorrection;
    }

    public boolean isBeforeJellyBean() {
        if (null == mPackageInfo || null == mPackageInfo.applicationInfo) {
            return false;
        }
        return mPackageInfo.applicationInfo.targetSdkVersion < VERSION_CODES.JELLY_BEAN;
    }

    @Override
    public String toString() {
        if (null == mPackageInfo || null == mPackageInfo.applicationInfo) {
            return "";
        }
        final StringBuilder s = new StringBuilder();
        s.append("Target application : ")
                .append(mPackageInfo.applicationInfo.name)
                .append("\nPackage : ")
                .append(mPackageInfo.applicationInfo.packageName)
                .append("\nTarget app sdk version : ")
                .append(mPackageInfo.applicationInfo.targetSdkVersion);
        return s.toString();
    }
}
