package com.gebeya.mobile.bidir.ui.home.main.screenings;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.backend.sync.SyncCallback;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.screening.ComplexScreeningSyncState;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.local.ClientLocalSource;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.local.ComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.local.GroupedComplexScreeningLocalSource;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.data.groups.local.GroupLocalSource;
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

/**
 * Implementation of the Screenings presenter
 */
public class ScreeningsPresenter implements ScreeningsContract.Presenter, SyncCallback {

    private ScreeningsContract.View view;

    private static final String title = "Screenings";

    @Inject ClientLocalSource clientLocal;
    @Inject ComplexScreeningLocalSource screeningLocal;
    @Inject SchedulersProvider schedulers;
    @Inject ComplexScreeningSyncState syncState;
    @Inject PreferenceHelper preference;
    @Inject UserLocalSource userLocal;
    @Inject GroupLocalSource groupLocal;
    @Inject GroupedComplexScreeningLocalSource groupScreeningLocal;

    private Group group;

    private List<Group> groups;
    private List<GroupedComplexScreening> groupedComplexScreenings;
    private final List<ComplexScreening> screenings;

    public ScreeningsPresenter() {
        Tooth.inject(this, Scopes.SCOPE_LOCAL_SOURCES);
        screenings = new ArrayList<>();
        groups = new ArrayList<>();
        groupedComplexScreenings = new ArrayList<>();
    }

    @Override
    public void start() {
        if (preference.getIndividual()) {
            loadScreenings();
        } else {
            loadGroupApplication();
//            loadGroupedScreeningApplication();
        }
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupApplication() {
        userLocal.first()
                .flatMap(data -> groupLocal.getAllScreenings())
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupScreeningsLabel(this.groups.isEmpty());
                    view.toggleNoScreeningsLabel(false);
                });
        syncState.addSyncCallback(this);
        loadSyncStatus();
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadScreenings() {
        screeningLocal.getAll()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    screenings.clear();
                    screenings.addAll(data);
                    view.showScreenings();
                    view.toggleNoScreeningsLabel(screenings.isEmpty());
                    view.toggleNoGroupScreeningsLabel(false);
                });

        syncState.addSyncCallback(this);
        loadSyncStatus();
    }

    @SuppressLint("CheckResult")
    private void loadSyncStatus() {
        screeningLocal.hasUnSyncedData()
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
    public void onSyncPressed() {
        if (view == null) return;
        view.startSyncService();
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
        loadScreenings();
    }

    @Override
    public void onScreeningSelected(int position) {
        final ComplexScreening screening = screenings.get(position);
        view.openScreening(screening._id);
    }

    @Override
    public void onGroupSelected(int position) {
        final Group group = groups.get(position);
        view.openGroupedScreeningScreen(group, title);

    }

    @Override
    public int getGroupCount() {
//        return groupedComplexScreenings.size();
        return groups.size();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onGroupBindRowView(ScreeningsContract.GroupItemView holder, int position) {
        final Group group = groups.get(position);

        holder.setGroupName(group.groupName);
        holder.setGroupCount(group.membersCount);
        groupScreeningLocal.getByGroupId(group._id)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    if (data.empty())
                        return;
                    GroupedComplexScreening screening = data.get();
                    holder.setStatus(screening.status);
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

//        final GroupedComplexScreening groupedScreening = groupedComplexScreenings.get(position);
//        holder.setStatus(groupedScreening.status);
//
//        groupLocal.get(groupedScreening.groupId)
//                .subscribeOn(schedulers.background())
//                .observeOn(schedulers.ui())
//                .subscribe(groupData -> {
//                    group = groupData.get();
//                    holder.setGroupName(group.groupName);
//                    holder.setGroupCount(group.membersCount);
//
//                    if (group.leaderId == null) {
//                        holder.setLeaderName(null);
//                        return;
//                    }
//
//                    clientLocal.get(group.leaderId)
//                            .subscribeOn(schedulers.background())
//                            .observeOn(schedulers.ui())
//                            .subscribe(clientData -> {
//                                if (clientData.empty())
//                                    return;
//                                Client leader = clientData.get();
//
//                                final String name = Utils.formatName(
//                                        leader.firstName,
//                                        leader.lastName,
//                                        leader.surname
//                                );
//
//                                holder.setLeaderName(name);
//                                holder.setImage(leader.photoUrl);
//                            });
//                });


    }

    @Override
    public void onScreeningFilterClicked() {
        if (preference.getIndividual()) {
            view.openIndividualFilterMenu();
        } else {
            view.openGroupFilterMenu();
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadNewScreenings() {
        screeningLocal.getNewScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    screenings.clear();
                    screenings.addAll(data);
                    view.showScreenings();
                    view.toggleNoScreeningsLabel(screenings.isEmpty());
                    view.toggleNoGroupScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInprogressScreenings() {
        screeningLocal.getInprogressScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    screenings.clear();
                    screenings.addAll(data);
                    view.showScreenings();
                    view.toggleNoScreeningsLabel(screenings.isEmpty());
                    view.toggleNoGroupScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadSubmittedScreenings() {
        screeningLocal.getSubmittedScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    screenings.clear();
                    screenings.addAll(data);
                    view.showScreenings();
                    view.toggleNoScreeningsLabel(screenings.isEmpty());
                    view.toggleNoGroupScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadEligibleScreenings() {
        screeningLocal.getEligibleScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    screenings.clear();
                    screenings.addAll(data);
                    view.showScreenings();
                    view.toggleNoScreeningsLabel(screenings.isEmpty());
                    view.toggleNoGroupScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadRejectedScreenings() {
        screeningLocal.getRejectedScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(data -> {
                    screenings.clear();
                    screenings.addAll(data);
                    view.showScreenings();
                    view.toggleNoScreeningsLabel(screenings.isEmpty());
                    view.toggleNoGroupScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedNewScreenings() {
        groupLocal.getAllNewGroups()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupScreeningsLabel(this.groups.isEmpty());
                    view.toggleNoScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedInprogressScreenings() {
        groupLocal.getGroupedInProgressScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupScreeningsLabel(this.groups.isEmpty());
                    view.toggleNoScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedSubmittedScreenings() {
        groupLocal.getGroupedSubmittedScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupScreeningsLabel(this.groups.isEmpty());
                    view.toggleNoScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedEligibleScreenings() {
        groupLocal.getGroupedEligibleScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupScreeningsLabel(this.groups.isEmpty());
                    view.toggleNoScreeningsLabel(false);
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadGroupedRejectedScreenings() {
        groupLocal.getGroupedRejectedScreenings()
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(groups -> {
                    this.groups.clear();
                    for (Group group : groups) {
                        if (group.membersCount == group.clients.size())
                            this.groups.add(group);
                    }
                    view.showGrouped();
                    view.toggleNoGroupScreeningsLabel(this.groups.isEmpty());
                    view.toggleNoScreeningsLabel(false);
                });
    }

    @Override
    @SuppressLint("CheckResult")
    public void onBindRowView(@NonNull ScreeningsContract.ComplexScreeningItemView holder, int position) {

        final ComplexScreening screening = screenings.get(position);
        holder.setStatus(screening.status);

        clientLocal.get(screening.clientId)
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
                        holder.setName(name);
                        holder.setImage(client.photoUrl);
                    } else {
                        Utils.e(this, "No client was found locally! Should probably use fetch method instead");
                    }
                });
    }

    @Override
    public int getScreeningCount() {
        return screenings.size();
    }

    @Override
    public void onIndividualButtonPressed() {
        preference.setIndividual(true);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadScreenings();
    }

    @Override
    public void onGroupButtonPressed() {
        preference.setIndividual(false);
        view.toggleIndividualButton(preference.getIndividual());
        view.toggleGroupButton(!preference.getIndividual());
        loadGroupApplication();
//        loadGroupedScreeningApplication();
    }

    @Override
    public void attachView(ScreeningsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        syncState.removeSyncCallback(this);
        view = null;
    }

    @Override
    public ScreeningsContract.View getView() {
        return view;
    }
}