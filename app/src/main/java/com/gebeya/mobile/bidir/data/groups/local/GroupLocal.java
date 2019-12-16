package com.gebeya.mobile.bidir.data.groups.local;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.BaseLocalSource;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.Client_;
import com.gebeya.mobile.bidir.data.gpslocation.remote.GPSLocationResponseParser;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.GroupRepo;
import com.gebeya.mobile.bidir.data.groups.Group_;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.impl.rx.Data;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.Collections;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Local implementation for the {@link GroupLocalSource} interface contract.
 */
public class GroupLocal extends BaseLocalSource implements GroupLocalSource {

    private final Box<Group> box;
    private final Box<Client> clientBox;

    public GroupLocal() {
        Tooth.inject(this, Scopes.SCOPE_ROOT);
        box = store.boxFor(Group.class);
        clientBox = store.boxFor(Client.class);
    }

    @Override
    public Completable removeAll() {
        box.removeAll();
        clientBox.query().notEqual(Client_.groupId, 0).build().remove();
        return Completable.complete();
    }

    @Override
    public Completable remove(int position) {
        final List<Group> groups = box.getAll();

        if (!groups.isEmpty()) {
            clientBox.query().equal(Client_.groupId, groups.get(position).id).build().remove();
            box.remove(groups.get(position));
        }

        return Completable.complete();
    }

    @Override
    public Completable remove(@NonNull String id) {
        final List<Group> groups = box.find(Group_._id, id);

        if (!groups.isEmpty()) {
            clientBox.query().equal(Client_.groupId, groups.get(0).id).build().remove();
            for (Client client : groups.get(0).clients) {
                clientBox.query().equal(Client_._id, client._id).build().remove();
            }
        }

        box.query().equal(Group_._id, id).build().remove();
        return Completable.complete();
    }

    @Override
    public Observable<Data<Group>> get(@NonNull String id) {
        final List<Group> groups = box.find(Group_._id, id);

        return Observable.just(new Data<>(groups.isEmpty() ? null : groups.get(groups.size() - 1)));
    }

    @Override
    public Observable<Data<Group>> get(int position) {
        final List<Group> groups = box.getAll();
        return Observable.just(new Data<>(groups.isEmpty() ? null : groups.get(position)));
    }

    @Override
    public Observable<Data<Group>> first() {
        return get(0);
    }

    @Override
    public int size() {
        return (int) box.count();
    }

    @Override
    public Observable<List<Group>> putAll(@NonNull List<Group> groups) {
        box.removeAll();
        box.put(groups);
        return Observable.just(groups);
    }

    @Override
    public Observable<Group> put(@NonNull Group group) {
        remove(group._id);
        box.put(group);
        return Observable.just(group);
    }

    @Override
    public Observable<List<Group>> getAll() {
        final List<Group> groups = box.getAll();
        Collections.sort(groups, new GroupComparator());
        return Observable.just(groups);
    }


    @Override
    public Observable<List<Group>> getAllScreenings() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.NEW)
                .or()
                .equal(Group_.groupStatus, GroupParser.SCREENING_IN_PROGRESS)
                .or()
                .equal(Group_.groupStatus, GroupParser.SCREENING_SUBMITTED)
                .or()
                .equal(Group_.groupStatus, GroupParser.ELIGIBLE)
                .or()
                .equal(Group_.groupStatus, GroupParser.INELIGIBLE)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getAllLoanApplications() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_NEW)
                .or()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_IN_PROGRESS)
                .or()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_SUBMITTED)
                .or()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_DECLINED)
                .or()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_ACCEPTED)
                .sort(new GroupComparator())
                .build()
                .find();
        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getAllACATs() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPRAISAL_IN_PROGRESS)
                .or()
                .equal(Group_.groupStatus, GroupParser.ACAT_NEW)
                .or()
                .equal(Group_.groupStatus, GroupParser.ACAT_IN_PROGRESS)
                .or()
                .equal(Group_.groupStatus, GroupParser.ACAT_SUBMITTED)
                .or()
                .equal(Group_.groupStatus, GroupParser.ACAT_DECLINED_FOR_REVIEW)
                .or()
                .equal(Group_.groupStatus, GroupParser.ACAT_RESUBMITTED)
                .or()
                .equal(Group_.groupStatus, GroupParser.ACAT_AUTHORIZED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getAllNewGroups() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.NEW)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedInProgressScreenings() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.SCREENING_IN_PROGRESS)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedSubmittedScreenings() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.SCREENING_SUBMITTED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedEligibleScreenings() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.ELIGIBLE)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedRejectedScreenings() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.INELIGIBLE)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedNewLoans() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_NEW)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedInprogressLoans() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_IN_PROGRESS)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedSubmittedLoans() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_SUBMITTED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedAcceptedLoans() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_ACCEPTED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedDeclinedLoans() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.LOAN_APPLICATION_DECLINED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedNewACATs() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.ACAT_NEW)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedInprogressACATs() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.ACAT_IN_PROGRESS)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedSubmittedACATs() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.ACAT_SUBMITTED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedResubmittedACATs() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.ACAT_RESUBMITTED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedAuthorizedACATs() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.ACAT_AUTHORIZED)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }

    @Override
    public Observable<List<Group>> getGroupedInReviewACATs() {
        final List<Group> groups = box.query()
                .equal(Group_.groupStatus, GroupParser.ACAT_DECLINED_FOR_REVIEW)
                .sort(new GroupComparator())
                .build()
                .find();

        return Observable.just(groups);
    }
}
