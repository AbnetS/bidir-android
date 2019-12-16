package com.gebeya.mobile.bidir.data.complexscreening.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.DeleteOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Contract interface for a local source for complex screenings.
 */
public interface ComplexScreeningLocalSource extends
        WritableSource<ComplexScreening>,
        ReadableSource<ComplexScreening>,
        DeleteOne,
        ReadSize {

    /**
     * Mark the given screening ready for upload.
     *
     * @param screening The complex screening to mark.
     * @return Observable of the marked screening object.
     */
    Observable<ComplexScreening> markForUpload(@NonNull ComplexScreening screening);

    /**
     * Get a list of all the non uploaded {@link ComplexScreening} objects.
     *
     * @return List of the complex screening objects that need to be uploaded.
     */
    List<ComplexScreening> getAllModifiedNonUploaded();

    /**
     * Returns an observable that represents whether there is data that needs to be synced or not.
     *
     * @return Observable containing the boolean result
     */
    Observable<Boolean> hasUnSyncedData();


    /**
     * Get a list of {@link ComplexScreening}s that have been created but still need to be uploaded.
     *
     * @return List of the found screenings.
     */
    List<ComplexScreening> getAllPendingCreationNonUploaded();

    Completable updateWithLocalIds(List<ComplexScreening> remoteScreenings);

    /**
     * Get new screenings.
     *
     * @return
     */
    Observable<List<ComplexScreening>> getNewScreenings();

    /**
     * Get screenings with in progress status.
     * @return
     */
    Observable<List<ComplexScreening>> getInprogressScreenings();

    /**
     * Get screenings that are submitted.
     *
     * @return
     */
    Observable<List<ComplexScreening>> getSubmittedScreenings();

    /**
     * Get Screenings that have eligible status.
     */
    Observable<List<ComplexScreening>> getEligibleScreenings();

    /**
     * Get Screenings that are rejected.
     * @return
     */
    Observable<List<ComplexScreening>> getRejectedScreenings();
}
