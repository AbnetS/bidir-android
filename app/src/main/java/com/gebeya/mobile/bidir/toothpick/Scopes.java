package com.gebeya.mobile.bidir.toothpick;

import com.gebeya.mobile.bidir.data.user.remote.UserRemoteSource;
import com.gebeya.mobile.bidir.toothpick.modules.RepositoriesModule;

/**
 * Interface defining a list of named {@link toothpick.Scope}s as Strings which Toothpick can use
 * for injection
 */
public interface Scopes {
    /**
     * Scope representing the {@link android.app.Application}, which is also the root level.
     * This scope also contains other tools that all the other modules might need.
     * All the other scopes inherit from this scope directly or indirectly.
     *
     * @see com.gebeya.mobile.bidir.toothpick.modules.RootModule
     */
    String SCOPE_ROOT = "ROOT";

    /**
     * Scope representing the local sources of data, such as the
     * {@link com.gebeya.mobile.bidir.data.user.local.UserLocalSource} one.
     * <p>
     * Inherits from {@link Scopes#SCOPE_ROOT} because local sources need ObjectBox found in the
     * root scope.
     *
     * @see com.gebeya.mobile.bidir.toothpick.modules.LocalSourcesModule
     */
    String SCOPE_LOCAL_SOURCES = "LOCAL_SOURCES";

    /**
     * Scope representing the remote sources of data, such as the
     * {@link UserRemoteSource} one.
     * <p>
     * Inherits from {@link Scopes#SCOPE_LOCAL_SOURCES} because remote sources need the user local
     * source to getByType the auth token injection.
     *
     * @see com.gebeya.mobile.bidir.toothpick.modules.RemoteSourcesModule
     */
    String SCOPE_REMOTE_SOURCES = "REMOTE_SOURCES";

    /**
     * Scope level for repositories.
     * Inherits from {@link Scopes#SCOPE_REMOTE_SOURCES} because repositories need all the sources
     * in order to work.
     *
     * @see RepositoriesModule
     */
    String SCOPE_REPOSITORIES = "REPOSITORIES";

    /**
     * Scope level for states.
     * Inherits from {@link Scopes#SCOPE_REPOSITORIES} to ease implementations.
     */
    String SCOPE_STATES = "STATES";
}