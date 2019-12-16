package com.gebeya.mobile.bidir.toothpick;

import android.support.annotation.NonNull;

import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Helper class for performing injections via the {@link toothpick.Toothpick} library
 */
public final class Tooth {

    /**
     * Opens a given scope with the given name and performs an injection within that scope,
     * using {@link Toothpick}. If the scope doesn't exist, Toothpick creates a new one.
     *
     * @param scope  String name of the scope to open, using Toothpick.
     * @param target Object that is the target, whose fields are marked with @{@link javax.inject.Inject}
     */
    public static void inject(@NonNull Object target, @NonNull String scope) {
        final Scope s = Toothpick.openScope(scope);
        Toothpick.inject(target, s);
    }
}