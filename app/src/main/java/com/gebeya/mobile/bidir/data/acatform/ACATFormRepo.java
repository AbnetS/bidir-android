package com.gebeya.mobile.bidir.data.acatform;

import com.gebeya.mobile.bidir.data.acatform.local.ACATFormLocalSource;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormRemote;
import com.gebeya.mobile.bidir.data.acatform.remote.ACATFormResponse;
import com.gebeya.mobile.bidir.data.crop.local.CropLocalSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Repository implementation for ACATForms, via the {@link ACATFormRepoSource} interface.
 */

public class ACATFormRepo implements ACATFormRepoSource {

    @Inject
    ACATFormLocalSource local;

    @Inject
    ACATFormRemote remote;

    @Inject
    CropLocalSource cropLocal;

    public ACATFormRepo() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @Override
    public Observable<List<ACATForm>> fetchAll() {
        return local.getAll()
                .flatMap(forms -> forms.isEmpty() ? fetchForceAll() : Observable.just(forms));
    }

    @Override
    public Observable<List<ACATForm>> fetchForceAll() {
        return remote.getAll()
                .flatMap(responses -> {
                    for (ACATFormResponse response: responses) {
                        local.put(response.acatForm);
                        cropLocal.put(response.crop);
                    }
                    return local.getAll();
                });
    }
}
