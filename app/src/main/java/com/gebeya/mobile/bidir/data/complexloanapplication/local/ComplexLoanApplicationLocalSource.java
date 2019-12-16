package com.gebeya.mobile.bidir.data.complexloanapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.DeleteOne;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Contract interface for a local source for complex loan applications.
 */
public interface ComplexLoanApplicationLocalSource extends
        WritableSource<ComplexLoanApplication>,
        ReadableSource<ComplexLoanApplication>,
        DeleteOne,
        ReadSize {

    /**
     * Mark the given complex loan application, ready for upload.
     *
     * @param application The loan application to mark for upload.
     * @return Observable for the prepared complex loan application.
     */
    Observable<ComplexLoanApplication> markForUpload(@NonNull ComplexLoanApplication application);

    /**
     * Get a list of all the non uploaded complex loan applications.
     *
     * @return List of all the loan applications that need to be uploaded.
     */
    List<ComplexLoanApplication> getAllModifiedNonUploaded();

    /**
     * Get a list of complex loan applications based on the given status.
     *
     * @param status Status to use as the filter.
     * @return List of found complex loan applications.
     */
    Observable<List<ComplexLoanApplication>> getByStatus(@NonNull String status);

    Completable updateWithLocalIds(List<ComplexLoanApplication> remoteLoanApplications);

    /**
     * Get a list of all new loan applications.
     *
     * @return
     */
    Observable<List<ComplexLoanApplication>> getNewLoans();

    /**
     * Get a list of all in progress loan applications.
     *
     * @return
     */
    Observable<List<ComplexLoanApplication>> getInprogressLoans();

    /**
     * Get a list of all submitted loan applications.
     *
     * @return
     */
    Observable<List<ComplexLoanApplication>> getSubmittedLoans();

    /**
     * Get a list of all approved loan applications.
     *
     * @return
     */
    Observable<List<ComplexLoanApplication>> getApprovedLoans();

    /**
     * Get a list of all declined loan applications.
     *
     * @return
     */
    Observable<List<ComplexLoanApplication>> getDeclinedLoans();
}
