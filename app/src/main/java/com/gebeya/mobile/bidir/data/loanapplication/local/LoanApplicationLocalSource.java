package com.gebeya.mobile.bidir.data.loanapplication.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplication;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract for managing local source for {@link com.gebeya.mobile.bidir.data.loanapplication.LoanApplication}
 */
public interface LoanApplicationLocalSource extends
        WritableSource<LoanApplication>,
        ReadableSource<LoanApplication>,
        ReadSize {

    /**
     * Return a list of loan applications belonging to a single client
     */
    Observable<List<LoanApplication>> getAllByClient(@NonNull String clientId);

    Observable<List<LoanApplication>> getAcceptedClient(@NonNull String status);
}