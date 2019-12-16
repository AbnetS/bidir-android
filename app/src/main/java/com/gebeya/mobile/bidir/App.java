package com.gebeya.mobile.bidir;

import android.support.multidex.MultiDexApplication;

import com.gebeya.apps.framework.data.prefs.BasePrefs;
import com.gebeya.mobile.bidir.data.MyObjectBox;
import com.gebeya.mobile.bidir.impl.util.Fonts;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.modules.LocalSourcesModule;
import com.gebeya.mobile.bidir.toothpick.modules.RemoteSourcesModule;
import com.gebeya.mobile.bidir.toothpick.modules.RepositoriesModule;
import com.gebeya.mobile.bidir.toothpick.modules.RootModule;
import com.gebeya.mobile.bidir.toothpick.modules.StatesModule;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import toothpick.Toothpick;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.initializeFabric(getApplicationContext());
        Utils.e(this, "< ---------------------------------------- App.onCreate() ---------------------------------------- >");

        Fonts.initialize(getApplicationContext());  // TODO: Move this to injection level

        final BoxStore store = MyObjectBox.builder().androidContext(this).build();
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(store).start(this);
        }

        // Opens the ROOT scope and initializes everything the scope.
        Toothpick.openScope(
                Scopes.SCOPE_ROOT
        ).installModules(new RootModule(getApplicationContext(), store));

        /*
            Opens the ROOT scope and then the LOCAL_SOURCES scope. This order makes LOCAL_SOURCES
            scope inherit from the ROOT scope. Anything defined in the ROOT scope can be injected
            by just providing the LOCAL_SOURCES scope, since it is inherited. The right-most scope
            (in this case, the LOCAL_SOURCES scope) is the last one to be returned when opening.
         */
        Toothpick.openScopes(
                Scopes.SCOPE_ROOT,
                Scopes.SCOPE_LOCAL_SOURCES
        ).installModules(new LocalSourcesModule());

        /*
            Opens the LOCAL_SOURCES scope and then the REMOTE_SOURCES scope. This order makes
            REMOTE_SOURCES inherit from LOCAL_SOURCES (which in turn inherits from the ROOT scope).
            This means that anything defined in both LOCAL_SOURCES and ROOT is accessible from
            the REMOTE_SOURCES scope.
         */
        Toothpick.openScopes(
                Scopes.SCOPE_LOCAL_SOURCES,
                Scopes.SCOPE_REMOTE_SOURCES
        ).installModules(new RemoteSourcesModule());

        /*
            Opens the REPOSITORIES scope, which inherits from every other scopes as well.
            If a scope being opened doesn't exist, it is created. If it already exists, it is
            returned instead.
            The REPOSITORIES scope is used to keep track of the repository classes, as singletons.
         */
        Toothpick.openScopes(
                Scopes.SCOPE_REMOTE_SOURCES,
                Scopes.SCOPE_REPOSITORIES
        ).installModules(new RepositoriesModule());

        Toothpick.openScopes(
                Scopes.SCOPE_REPOSITORIES,
                Scopes.SCOPE_STATES
        ).installModules(new StatesModule());

        BasePrefs.initialize(getApplicationContext());
    }
}
