package com.gebeya.mobile.bidir.data.groups.local;


import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.DeletableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.groups.Group;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface contract for managing the grouped clients on the device
 */
public interface GroupLocalSource extends
        ReadOne<Group>,
        WritableSource<Group>,
        DeletableSource,
        ReadSize {

    /**
     * Return a list of all the {@link Group} objects only created by the logged in user.
     */
    Observable<List<Group>> getAll();

    /**
     * Return a list of all the {@link Group} objects under Screening only created by the logged in user.
     */
    Observable<List<Group>> getAllScreenings();

    /**
     * Return a list of all the {@link Group} objects under Loan Application only created by the logged in user.
     */
    Observable<List<Group>> getAllLoanApplications();

    /**
     * Return a list of all the {@link Group} objects under ACAT only created by the logged in user.
     */
    Observable<List<Group>> getAllACATs();

    /**
     * Return a list of all new Grouped applications.
     */
    Observable<List<Group>> getAllNewGroups();

    /**
     * Return a list of all new Grouped applications.
     */
    Observable<List<Group>> getGroupedInProgressScreenings();

    /**
     * Return a list of all submitted Grouped applications.
     */
    Observable<List<Group>> getGroupedSubmittedScreenings();

    /**
     * Return a list of all eligible Grouped applications.
     */
    Observable<List<Group>> getGroupedEligibleScreenings();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedRejectedScreenings();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedNewLoans();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedInprogressLoans();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedSubmittedLoans();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedAcceptedLoans();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedDeclinedLoans();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedNewACATs();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedInprogressACATs();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedSubmittedACATs();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedResubmittedACATs();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedAuthorizedACATs();

    /**
     * Return a list of all rejected Grouped applications.
     */
    Observable<List<Group>> getGroupedInReviewACATs();


}
