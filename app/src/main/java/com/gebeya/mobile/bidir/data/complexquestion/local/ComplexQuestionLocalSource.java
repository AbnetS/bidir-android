package com.gebeya.mobile.bidir.data.complexquestion.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface definition for {@link com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion}
 * local sources.
 */
public interface ComplexQuestionLocalSource extends WritableSource<ComplexQuestion> {

    /**
     * Get a list of all {@link ComplexQuestion} objects belonging to a given reference ID of the
     * given reference type.
     *
     * @param referenceId   Reference ID to use as a filter.
     * @param referenceType Reference type to use as a filter.
     * @return List of all the ComplexQuestion objects matching the filters.
     */
    Observable<List<ComplexQuestion>> getAll(@NonNull String referenceId, @NonNull String referenceType);

    /**
     * Retrieve a list of all the complex question request objects.
     *
     * @param referenceId   Reference ID to use as the lookup.
     * @param referenceType Reference type to use as the lookup.
     * @return Observable list of all the requests.
     */
    Observable<List<ComplexQuestionRequest>> getRequests(@NonNull String referenceId, @NonNull String referenceType);
}