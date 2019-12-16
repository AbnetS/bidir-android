package com.gebeya.mobile.bidir.data.acatcostsection.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by abuti on 5/12/2018.
 */

public interface ACATCostSectionLocalSource extends ReadableSource<ACATCostSection>, WritableSource<ACATCostSection> {

    Observable<List<ACATCostSection>> getByParent(@NonNull String parentId);

    Observable<ACATCostSection> markForUpload(@NonNull ACATCostSection section);

}
