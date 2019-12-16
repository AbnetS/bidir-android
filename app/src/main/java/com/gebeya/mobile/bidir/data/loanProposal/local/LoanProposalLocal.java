package com.gebeya.mobile.bidir.data.loanProposal.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal;
import com.gebeya.mobile.bidir.data.loanProposal.LoanProposal_;
import com.gebeya.mobile.bidir.data.loanProposal.remote.LoanProposalParser;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.List;

import io.objectbox.Box;
import io.reactivex.Observable;

/**
 * Created by Samuel K. on 8/16/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProposalLocal extends BaseLocalSource implements LoanProposalLocalSource {

    private final Box<LoanProposal> box;

    public LoanProposalLocal() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        box = store.boxFor(LoanProposal.class);
    }

    @Override
    public Observable<List<LoanProposal>> getAll() {
        return Observable.just(box.getAll());
    }

    @Override
    public Observable<LoanProposal> put(@NonNull LoanProposal loanProposal) {
        box.query().equal(LoanProposal_._id, loanProposal._id).build().remove();
        loanProposal.id = 0;
        box.put(loanProposal);
        return Observable.just(loanProposal);
    }

    @Override
    public Observable<List<LoanProposal>> putAll(@NonNull List<LoanProposal> loanProposals) {
        box.removeAll();
        box.put(loanProposals);
        return Observable.just(loanProposals);
    }

    @Override
    public Observable<Data<LoanProposal>> get(@NonNull String id) {
        final List<LoanProposal> loanProposals = box.find(LoanProposal_._id, id);
        return Observable.just(new Data<>(loanProposals.isEmpty() ? null : loanProposals.get(0)));
    }

    @Override
    public Observable<Data<LoanProposal>> get(int position) {
        final List<LoanProposal> loanProposals = box.getAll();
        return Observable.just(new Data<>(loanProposals.isEmpty() ? null : loanProposals.get(0)));
    }

    @Override
    public Observable<Data<LoanProposal>> first() {
        return get(0);
    }

    @Override
    public Observable<Data<LoanProposal>> getProposalByClient(@NonNull String clientId) {
        final List<LoanProposal> loanProposals = box.query()
                .equal(LoanProposal_.clientId, clientId)
                .and()
                .notEqual(LoanProposal_.status, LoanProposalParser.STATUS_LOCAL_LOAN_PAID)
                .build()
                .find();
        return Observable.just(new Data<>(loanProposals.isEmpty() ? null : loanProposals.get(loanProposals.size() - 1)));
    }

    @Override
    public Observable<Data<LoanProposal>> getProposalByACAT(@NonNull String acatId) {
        final List<LoanProposal> loanProposals = box.find(LoanProposal_.clientACATId, acatId);
        return Observable.just(new Data<>(loanProposals.isEmpty() ? null : loanProposals.get(0)));
    }

    @Override
    public int size() {
        return (int) box.count();
    }
}
