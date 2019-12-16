package com.gebeya.mobile.bidir.ui.home.main.clients;

import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.TextView;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;
import com.gebeya.mobile.bidir.ui.home.main.SyncablePresenter;
import com.gebeya.mobile.bidir.ui.home.main.SyncableView;

/**
 * Contract for the clients, located on the home screen, under
 * the {@link com.gebeya.mobile.bidir.ui.home.main.MainFragment}
 */
public interface ClientsContract {

    /**
     * Clients presenter interface contract
     */
    interface Presenter extends BasePresenter<View>, SyncablePresenter {
        void onBindRowView(ClientItemView holder, int position);
        void onGroupBindRowView(GroupItemView holder, int position);
        void onClientSelected(int position);
        void onGroupSelected(int position);
        void onAddButtonClicked();
        int getClientCount();
        int getGroupCount();

        void onCreateReturned(@NonNull String groupCode, int memberCount, double loanAmount);

        void onOptionMenuClicked(TextView optionMenu, int adapterPosition);
        void onGroupOptionMenuClicked(@NonNull TextView anchor, int position);

        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);
        void onGroupUpdateFailed(@NonNull Throwable throwable);

        void onLoadingComplete();
        void onLoadingFailed(@NonNull Throwable throwable);

        void onNewLoanCycleClicked(@NonNull Client client);

        void onIndividualButtonPressed();
        void onGroupButtonPressed();

        void onCreateComplete(@NonNull Group group);
        void onCreateFailed(@NonNull Throwable throwable);
        void onLoanPaidClicked(@NonNull Client client);
        void onSetLoanPaidReturned(@NonNull Client client);
        void onGroupNewLoanCycleClicked(@NonNull String groupId, @NonNull String loanAmount);
        void onGroupLoanPaidClicked(@NonNull Group group);
        void onGroupSetLoanPaidReturned(@NonNull Group group);
        void onRecordReturned(@NonNull String loanApproved, @NonNull String clientId);
        void onClientsFilterClicked();

        void loadIndividualClients();
        void loadGroupedClients();

        void loadNewClients();
        void loadScreeningClients();
        void loadLoanClients();
        void loadACATClients();
        void loadGrantedClients();
        void loadPaidClients();
        void loadGroupedNewClients();
        void loadGroupedScreeningClients();
        void loadGroupedLoanClients();
        void loadGroupedACATClients();
    }

    /**
     * Clients view interface contract
     */
    interface View extends BaseView<Presenter>, SyncableView {
        void showClients();
        void showGrouped();
        void refreshClients();
        void refreshGrouped();
        void toggleNoClientsLabel(boolean show);
        void openAddClientScreen();
        void toggleAddClientsButton(boolean show);
        void openClient(@NonNull Client client);

        void toggleIndividualMenuItems(@NonNull PopupMenu popupMenu, @NonNull Client client);
        void openPopUpMenu(@NonNull TextView tv, @NonNull Client client);

        void toggleGroupMenuItems(@NonNull PopupMenu popupMenu, @NonNull Group group);
        void openGroupPopUpMenu(@NonNull TextView anchor, @NonNull Group group);

        void openGroupedApplicationScreen(@NonNull Group group, @NonNull String title);

        void showGroupLoadingProgress();
        void showLoadingProgress();
        void hideLoadingProgress();
        void showTaskSuccessfulMessage();
        void showStatusChangedMessage();
        void showError(@NonNull Result result);
        void startScreeningSyncService();

        void toggleIndividualButton(boolean enabled);
        void toggleGroupButton(boolean enabled);

        void toggleNoGroupsLabel(boolean show);
        void openAddGroupScreen();
        void openConfirmationDialog(@Nullable Client client, @Nullable Group group);
        void showCompleteMessage();
        void showUpdatingClientProgress();

        void showUpdatingGroupProgress();
        void showUpdatingProgress();

        void openIndividualFilterMenu();

        void openGroupedFilterMenu();
    }

    /**
     * Interface representing a client item view
     */
    interface ClientItemView extends BaseMainItemView {

    }

    /**
     * Interface representing a group item view
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