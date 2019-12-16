package com.gebeya.mobile.bidir.ui.home.main.screenings;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;
import com.gebeya.mobile.bidir.ui.home.main.SyncablePresenter;
import com.gebeya.mobile.bidir.ui.home.main.SyncableView;


/**
 * Contract for the Screenings screen.
 */
public interface ScreeningsContract {

    /**
     * Screening presenter interface contract
     */
    interface Presenter extends BasePresenter<View>, SyncablePresenter {
        void loadScreenings();
        void loadGroupApplication();

        void onBindRowView(@NonNull ComplexScreeningItemView holder, int position);
        void onScreeningSelected(int position);
        int getScreeningCount();

        void onIndividualButtonPressed();
        void onGroupButtonPressed();

        void onGroupSelected(int position);

        int getGroupCount();

        void onGroupBindRowView(GroupItemView holder, int position);

        void onScreeningFilterClicked();

        void loadNewScreenings();
        void loadInprogressScreenings();
        void loadSubmittedScreenings();
        void loadEligibleScreenings();
        void loadRejectedScreenings();

        void loadGroupedNewScreenings();
        void loadGroupedInprogressScreenings();
        void loadGroupedSubmittedScreenings();
        void loadGroupedEligibleScreenings();
        void loadGroupedRejectedScreenings();


    }

    /**
     * Screening view interface contract
     */
    interface View extends BaseView<Presenter>, SyncableView {
        void showScreenings();
        void toggleNoScreeningsLabel(boolean show);
        void toggleNoGroupScreeningsLabel(boolean show);
        void openScreening(@NonNull String screeningId);

        void toggleIndividualButton(boolean isEnabled);
        void toggleGroupButton(boolean isEnabled);

        void openGroupedScreeningScreen(@NonNull Group group, @NonNull String title);

        void showGrouped();
        void openIndividualFilterMenu();
        void openGroupFilterMenu();
    }

    /**
     * Interface representing a complex screening item view
     */
    interface ComplexScreeningItemView extends BaseMainItemView {

    }

    /**
     * Interface representing grouped complex screening item view
     */
    interface GroupItemView {
        void setGroupName(@NonNull String groupName);
        void setLeaderName(@Nullable String leaderName);
        void setStatus(@NonNull String status);
        void setImage(@NonNull String pictureUrl);
        void setGroupCount(@NonNull int groupCount);
        void toggleCreatedIndicator(boolean show);
    }
}
