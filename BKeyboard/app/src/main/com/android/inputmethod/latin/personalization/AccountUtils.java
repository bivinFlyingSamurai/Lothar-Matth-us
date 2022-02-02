

package com.android.inputmethod.latin.personalization;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountUtils {
    private AccountUtils() {
        // This utility class is not publicly instantiable.
    }

    private static Account[] getAccounts(final Context context) {
        return AccountManager.get(context).getAccounts();
    }

    public static List<String> getDeviceAccountsEmailAddresses(final Context context) {
        final ArrayList<String> retval = new ArrayList<>();
        for (final Account account : getAccounts(context)) {
            final String name = account.name;
            if (Patterns.EMAIL_ADDRESS.matcher(name).matches()) {
                retval.add(name);
                retval.add(name.split("@")[0]);
            }
        }
        return retval;
    }

    /**
     * Get all device accounts having specified domain name.
     * @param context application context
     * @param domain domain name used for filtering
     * @return List of account names that contain the specified domain name
     */
    public static List<String> getDeviceAccountsWithDomain(
            final Context context, final String domain) {
        final ArrayList<String> retval = new ArrayList<>();
        final String atDomain = "@" + domain.toLowerCase(Locale.ROOT);
        for (final Account account : getAccounts(context)) {
            if (account.name.toLowerCase(Locale.ROOT).endsWith(atDomain)) {
                retval.add(account.name);
            }
        }
        return retval;
    }
}
