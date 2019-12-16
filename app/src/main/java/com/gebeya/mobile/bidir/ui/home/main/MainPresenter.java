package com.gebeya.mobile.bidir.ui.home.main;

import android.annotation.SuppressLint;

import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACADataDownloadState;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATDataDownloadService;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATDataDownloadServiceCallback;
import com.gebeya.mobile.bidir.data.user.UserRepoSource;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

/**
 * Implementation of the Main presenter interface
 */

public class MainPresenter implements MainContract.Presenter, ACATDataDownloadServiceCallback {

    private MainContract.View view;

    @Inject UserRepoSource repo;
    @Inject ACADataDownloadState state;

    public MainPresenter() {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @Override
    public void start() {
        ACATDataDownloadService.addCallback(this);
        if (state.busy()) {
            view.showProgressContainer();
        }
    }

    @Override
    public void onDownloadStarted(int totalPages) {
        if (view == null) return;

        view.showProgressContainer();
        view.updateProgress(0);
    }

    @Override
    public void onUpdate(int totalProgress, int totalPages) {
        if (view == null) return;

        double percentage = (double) totalProgress / totalPages * 100;

        view.updateProgress((int) percentage);
    }

    @Override
    public void onDownloadStopped() {
        if (view == null) return;

        view.hideProgressContainer();
        view.showDownloadCompleteMessage();
    }


    @Override
    @SuppressLint("all")
    public void onNavLogOutPressedPressed() {
        repo.logout().subscribe(() -> {
            if (view != null) {
                view.showLoggedOutMessage();
                view.openLoginScreen();
                view.close();
            }
        });
    }

    @Override
    public void onSearchIconPressed() {
        view.openSearch();
    }

    @Override
    public void onNotificationsIconPressed() {
        view.openNotificationsScreen();
    }

    @Override
    public void onFilterIconPressed() {
        view.openFilterMenu();
    }

    @Override
    public void onTabSelected(int position) {
        view.activateTab(position);
    }

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        ACATDataDownloadService.removeCallback(this);
        view = null;
    }

    @Override
    public MainContract.View getView() {
        return view;
    }
}
