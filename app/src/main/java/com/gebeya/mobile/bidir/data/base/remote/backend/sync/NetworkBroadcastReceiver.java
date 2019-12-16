package com.gebeya.mobile.bidir.data.base.remote.backend.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gebeya.apps.framework.util.Loggable;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATSyncService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.client.ClientSyncService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.loanapplication.ComplexLoanApplicationSyncService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.screening.ComplexScreeningSyncService;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.data.user.remote.UserService;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Broadcast receiver that is used to watch network connectivity. This is not the ideal way to do
 * this on the latest Android versions, so it will be changed to involve a Task scheduler (like the
 * one built into Firebase) instead.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver implements Loggable {

    @Inject BoxStore store;

    public NetworkBroadcastReceiver() {
        super();
        Tooth.inject(this, Scopes.SCOPE_ROOT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        d("-------------------->> Network Connectivity Changed <<--------------------");
        final boolean connected = Utils.connected(context);
        d("Connected: %s", connected);
        if (!connected) return;

        final Box<User> box = store.boxFor(User.class);
        if (box.count() == 0) {
            d("-> User not logged in. Skipping sync");
            return;
        }

        d("Starting Sync services...");
        context.startService(new Intent(context, ClientSyncService.class));
        context.startService(new Intent(context, ACATSyncService.class));

/*        context.startService(new Intent(context, ComplexScreeningSyncService.class));
        context.startService(new Intent(context, ComplexLoanApplicationSyncService.class));*/
    }

    @Override
    public void d(String message) {
        Utils.d(this, message);
    }

    @Override
    public void d(String message, Object... formatArgs) {
        Utils.d(this, message, formatArgs);
    }

    @Override
    public void e(String message) {
        Utils.e(this, message);
    }

    @Override
    public void e(String message, Object... formatArgs) {
        Utils.e(this, message, formatArgs);
    }
}