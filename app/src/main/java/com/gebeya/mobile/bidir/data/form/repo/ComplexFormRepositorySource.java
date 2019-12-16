package com.gebeya.mobile.bidir.data.form.repo;

import com.gebeya.mobile.bidir.data.base.repo.FetchMany;
import com.gebeya.mobile.bidir.data.base.repo.FetchOne;
import com.gebeya.mobile.bidir.data.form.ComplexForm;

/**
 * Repository contract definition for the complex form sources.
 */
public interface ComplexFormRepositorySource extends
        FetchOne<ComplexForm>,
        FetchMany<ComplexForm> {

}