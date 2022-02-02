

package com.android.inputmethod.dictionarypack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class EventHandler extends BroadcastReceiver {
    /**
     * Receives a intent broadcast.
     *
     * We receive every day a broadcast indicating that date changed.
     * Then we wait a random amount of time before actually registering
     * the download, to avoid concentrating too many accesses around
     * midnight in more populated timezones.
     * We receive all broadcasts here, so this can be either the DATE_CHANGED broadcast, the
     * UPDATE_NOW private broadcast that we receive when the time-randomizing alarm triggers
     * for regular update or from applications that want to test the dictionary pack, or a
     * broadcast from DownloadManager telling that a download has finished.
     * See inside of AndroidManifest.xml to see which events are caught.
     * Also @see {@link BroadcastReceiver#onReceive(Context, Intent)}
     *
     * @param context the context of the application.
     * @param intent the intent that was broadcast.
     */
    @Override
    public void onReceive(final Context context, final Intent intent) {
        intent.setClass(context, DictionaryService.class);
        context.startService(intent);
    }
}
