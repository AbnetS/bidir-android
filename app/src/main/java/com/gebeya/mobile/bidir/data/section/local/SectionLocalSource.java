package com.gebeya.mobile.bidir.data.section.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.base.WriteMany;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestionRequest;
import com.gebeya.mobile.bidir.data.section.Section;
import com.gebeya.mobile.bidir.data.section.SectionRequest;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract interface for a local source for {@link com.gebeya.mobile.bidir.data.section.Section}
 * objects.
 */
public interface SectionLocalSource extends WriteMany<Section> {
    /**
     * Get a list of all the Sections, under the given reference ID and the reference type.
     *
     * @param referenceId   Reference ID to act as a filter.
     * @param referenceType Reference type to act as a filter.
     * @return Observable list of all the Sections that match the given filters.
     */
    Observable<List<Section>> getAll(@NonNull String referenceId, @NonNull String referenceType);

    /**
     * Build and return a list of all the section objects, along with their required data.
     *
     * @param referenceId      Reference ID to act as a filter.
     * @param referenceType    Reference type to act as a filter.
     * @param questionRequests Question requests to use.
     * @return Observable list of all the built section request objects.
     */
    Observable<List<SectionRequest>> getRequests(@NonNull String referenceId, @NonNull String referenceType,
                                                 @NonNull List<ComplexQuestionRequest> questionRequests);
}