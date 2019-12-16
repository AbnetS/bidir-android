package com.gebeya.mobile.bidir.ui.home.main.loanapplications;

import android.annotation.SuppressLint;

import com.gebeya.mobile.bidir.data.base.remote.backend.sync.SyncCallback;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.loanapplication.ComplexLoanApplicationSyncState;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.ComplexLoanApplication;
import com.gebeya.mobile.bidir.data.complexloanapplication.local.ComplexLoanApplicationLocalSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.GroupedComplexLoan;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.local.GroupedComplexLoanLocalSource;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.data.user.local.UserLocalSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.impl.util.location.preference.PreferenceHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Concrete implementation for the loan applications presenter.
 */
public class LoanApplicationsPresenter implements LoanApplicationsContract.Presenter, SyncCallback {

    private static final String title = "Loan Applications";

    private LoanApplicationsContract.View view;

    private User user;
    private List<Group> groups;

    @Inject ClientLocalSource clientLocal;
    @Inject ComplexLoanApplicationLocalSource loanApplicationLocal;

    @Inject SchedulersProvider schedulers;

    @Inject ComplexLoanApplicationSyncState syncState;

    @Inject UserLocalSource userLocal;
    @Inject PreferenceHelper preference;
    @Inject GroupLocalSource groupLocal;
    @Inject GroupedComplexLoanLocalSource groupedLoanLocal;

    private final List<ComplexLoanApplication> applications;

    public LoanApplicationsPresenter() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        applications = new ArrayList<>();
        groups = new ArrayList<>();
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        if (preference.getIndividual()) {
            loadLoanApplication();
        } else {
            loadGroupApplication();
        }

        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());

        syncState.addSyncCallback(this);
        loadSyncStatus();
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadLoanApplication() {
        loanApplicationLocal.getAll()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    applications.clear();
                    applications.addAll(list);
                    view.showLoanApplications();
                    view.toggleNoLoanApplications(applications.isEmpty());
                    view.toggleNoGroupLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    public void loadGroupApplication() {
        userLocal.first()
                .flatMap(data -> groupLocal.getAllLoanApplications())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupLoanApplications(this.groups.isEmpty());
                    view.toggleNoLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedNewApplication() {
        groupLocal.getGroupedNewLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupLoanApplications(this.groups.isEmpty());
                    view.toggleNoLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedInprogressLoan() {
        groupLocal.getGroupedInprogressLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupLoanApplications(this.groups.isEmpty());
                    view.toggleNoLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedSubmittedLoan() {
        groupLocal.getGroupedSubmittedLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupLoanApplications(this.groups.isEmpty());
                    view.toggleNoLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedApprovedLoan() {
        groupLocal.getGroupedAcceptedLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupLoanApplications(this.groups.isEmpty());
                    view.toggleNoLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loanGroupedDeclinedLoan() {
        groupLocal.getGroupedDeclinedLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupLoanApplications(this.groups.isEmpty());
                    view.toggleNoLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadNewApplication() {
        loanApplicationLocal.getNewLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    applications.clear();
                    applications.addAll(list);
                    view.showLoanApplications();
                    view.toggleNoLoanApplications(applications.isEmpty());
                    view.toggleNoGroupLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInprogressLoan() {
        loanApplicationLocal.getInprogressLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    applications.clear();
                    applications.addAll(list);
                    view.showLoanApplications();
                    view.toggleNoLoanApplications(applications.isEmpty());
                    view.toggleNoGroupLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadSubmittedLoan() {
        loanApplicationLocal.getSubmittedLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    applications.clear();
                    applications.addAll(list);
                    view.showLoanApplications();
                    view.toggleNoLoanApplications(applications.isEmpty());
                    view.toggleNoGroupLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadApprovedLoan() {
        loanApplicationLocal.getApprovedLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    applications.clear();
                    applications.addAll(list);
                    view.showLoanApplications();
                    view.toggleNoLoanApplications(applications.isEmpty());
                    view.toggleNoGroupLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loanDeclinedLoan() {
        loanApplicationLocal.getDeclinedLoans()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    applications.clear();
                    applications.addAll(list);
                    view.showLoanApplications();
                    view.toggleNoLoanApplications(applications.isEmpty());
                    view.toggleNoGroupLoanApplications(false);
                });
    }

    @SuppressLint("CheckResult")
    private void loadSyncStatus() {
        Observable.just(loanApplicationLocal.getAllModifiedNonUploaded())
                .map(list -> !list.isEmpty())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(hasUnSyncedData -> {
                    if (syncState.busy()) {
                        view.showSyncInProgress();
                    } else {
                        view.showSyncIdle(hasUnSyncedData);
                    }
                });
    }

    @Override
    @SuppressLint("CheckResult")
    public void onSyncStarted() {
        Completable.complete()
                .observeOn(schedulers.ui())
                .subscribe(() -> view.showSyncInProgress());
    }

    @Override
    public void onSyncStopped() {
        loadSyncStatus();
    }

    @Override
    public void onSyncPressed() {
        if (view == null) return;
        view.startSyncService();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onBindRowView(LoanApplicationsContract.LoanApplicationItemView view, int position) {
        final ComplexLoanApplication application = applications.get(position);
        view.setStatus(application.status);

        userLocal.first()
                .flatMap(data -> {
                    user = data.get();
                    return clientLocal.get(application.clientId);
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (!data.empty()) {

                        final Client client = data.get();
                        final String name = Utils.formatName(
                                client.firstName,
                                client.lastName,
                                client.surname
                        );
                        view.setName(name);
                        view.setImage(client.photoUrl);

                    } else {
                        Utils.e(LoanApplicationsPresenter.this, "No client was found! Should probably use fetch method instead");
                    }
                });
    }

    @Override
    public void onIndividualButtonPressed() {
        preference.setIndividual(true);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadLoanApplication();
    }

    @Override
    public void onGroupButtonPressed() {
        preference.setIndividual(false);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadGroupApplication();
        view.toggleNoLoanApplications(false);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupSelected(int position) {
        final Group group = groups.get(position);
        view.openGroupedLoanApplication(group, title);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupBindRowView(LoanApplicationsContract.GroupItemView holder, int position) {
        final Group group = groups.get(position);

        holder.setGroupName(group.groupName);
        holder.setGroupCount(group.membersCount);

        groupedLoanLocal.getByGroupId(group._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (data.empty())
                        return;
                    GroupedComplexLoan loan = data.get();
                    holder.setStatus(loan.status);
                });

        if (group.leaderId == null) {
            holder.setLeaderName(null);
            return;
        }

        clientLocal.get(group.leaderId)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(clientData -> {

                    Client leader = clientData.get();

                    final String name = Utils.formatName(
                            leader.firstName,
                            leader.lastName,
                            leader.surname
                    );

                    holder.setLeaderName(name);
                    holder.setImage(leader.photoUrl);

                });
    }

    @Override
    public void onLoansFilterMenuClicked() {
        if (preference.getIndividual()) {
            view.openIndividualFilterMenu();
        } else {
            view.openGroupFilterMenu();
        }

    }

    @Override
    public void onLoanApplicationSelected(int position) {
        final ComplexLoanApplication application = applications.get(position);
        view.openLoanApplication(application._id);
    }

    @Override
    public void attachView(LoanApplicationsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        syncState.removeSyncCallback(this);
        view = null;
    }

    @Override
    public LoanApplicationsContract.View getView() {
        return view;
    }

    @Override
    public int getLoanApplicationCount() {
        return applications.size();
    }

}