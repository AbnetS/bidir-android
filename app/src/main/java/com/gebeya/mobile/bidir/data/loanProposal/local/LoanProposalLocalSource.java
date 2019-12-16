package com.gebeya.mobile.bidir.data.loanProposal.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.WritableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.impl.rx.Data;

import io.reactivex.Observable;

/**
 *
 *
 *
 * samkura47@gmail.com
 */

public interface LoanProposalLocalSource extends ReadableSource<LoanProposal>, WritableSource<LoanProposal>, ReadSize{

    Observable<Data<LoanProposal>> getProposalByClient(@NonNull String clientId);

    Observable<Data<LoanProposal>> getProposalByACAT(@NonNull String acatId);

}
