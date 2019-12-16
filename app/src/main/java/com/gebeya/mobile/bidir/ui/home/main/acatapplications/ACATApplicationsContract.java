package com.gebeya.mobile.bidir.ui.home.main.acatapplications;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;
import com.gebeya.mobile.bidir.ui.home.main.SyncablePresenter;
import com.gebeya.mobile.bidir.ui.home.main.SyncableView;
import com.gebeya.mobile.bidir.ui.home.main.screenings.ScreeningsContract;

/**
 * Interface for ACAT application.
 */

public interface ACATApplicationsContract {

    interface Presenter extends BasePresenter<View>, SyncablePresenter {
        void onBindRowView(@NonNull ACATApplicationItemView holder, int position);
        void onACATApplicationSelected(int position);
        int getACATApplicationCount();

        void onOptionMenuClicked(TextView anchor, int position);

        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

        void onIndividualButtonPressed();
        void onGroupButtonPressed();

        void onGroupSelected(int position);
        int groupCount();
        void onGroupBindRowView(GroupItemView holder, int position);

        void onACATsFilterClicked();

        void loadACATs();

        void loadNewACATs();
        void loadInprogressACATs();
        void loadSubmittedACATs();
        void loadResubmittedACATs();
        void loadApprovedACATs();
        void loadDeclinedForReviewACATs();

        void loadGroupedApplications();
        void loadGroupedNewACATs();
        void loadGroupedInprogressACATs();
        void loadGroupedSubmittedACATs();
        void loadGroupedResubmittedACATs();
        void loadGroupedApprovedACATs();
        void loadGroupedDeclinedForReviewACATs();
    }

    interface View extends BaseView<Presenter>, SyncableView {
        void showACATApplications();
        void refreshACATApplications();
        void toggleNoACATApplications(boolean show);
        void toggleNoGroupACATApplications(boolean show);
        void openACATApplicationInitializer(@NonNull String clientId);

        void openACATPreliminaryScreen(@NonNull String clientId);

        void openPopUpMenu(android.view.View view, @NonNull Client client);

        void openCropList(@NonNull String clientId);

        void showError(@NonNull Result result);

        void showUpdatingProgress();
        void hideUpdatingProgress();

        void showUpdateSuccessfulMessage();

        void toggleIndividualButton(boolean isEnabled);
        void toggleGroupButton(boolean isEnabled);

        void openGroupedACATScreen(@NonNull Group group, @NonNull String title);
        void showGrouped();

        void showDownloadProgress();
        void hideDownloadProgress();

        void openIndividualFilterMenu();
        void openGroupedFilterMenu();
    }

    interface ACATApplicationItemView extends BaseMainItemView {

    }

    interface GroupItemView {
        void setGroupName(@NonNull String groupName);
        void setLeaderName(@Nullable String leaderName);
        void setStatus(@NonNull String status);
        void setImage(@NonNull String pictureUrl);
        void setGroupCount(@NonNull int groupCount);
        void toggleCreatedIndicator(boolean show);
    }
}
