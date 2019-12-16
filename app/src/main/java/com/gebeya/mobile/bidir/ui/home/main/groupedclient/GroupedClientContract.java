package com.gebeya.mobile.bidir.ui.home.main.groupedclient;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.widget.TextView;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;

/**
 * Interface class for Grouped Application.
 */
public interface GroupedClientContract {

    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull GroupedApplicationItemView holder, int position);
        void onClientSelected(int position);
        int getClientCount();
        void onOptionMenuClicked(@NonNull TextView optionMenu, int position);
        void onAddButtonClicked();

        void onAddGroupMemberReturned(boolean isNew);
        void onLoadingComplete();
        void onLoadingFailed(@NonNull Throwable throwable);

        void onRequestComplete();
        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);
        void onAssignLeaderClicked(@NonNull Client client);

        void onLoanPaidClicked(@NonNull Client client);
        void onSetLoanPaidReturned(@NonNull Client client);
        void onRecordReturned(@NonNull String loanApproved, @NonNull String clientId);
        void onBackButtonPressed();
    }

    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull String title);
        void showClients();
        void toggleNoClientsLabel(boolean show);

        void toggleClientAddButton(boolean show);

        void showLoadingProgress();

        void showUpdateProgress();

        void hideUpdateProgress();

        void showError(@NonNull Result error);

        void showUpdateSuccessfulMessage();

        void toggleMenuItems(@NonNull PopupMenu popupMenu, @NonNull Client client, @NonNull Group group);

        void openPopUpMenu(@NonNull TextView tv, @NonNull Client client, @NonNull Group group);

        void openAddClientScreen(@NonNull Group group);

        void openClient(@NonNull Client client);

        void openAddMemberChoiceScreen();

        void openSelectClientScreen(@NonNull Group group);

        void openConfirmationDialog(@NonNull Client client);

        void showUpdatingClientProgress();

        void showUpdatingProgress();
    }

    interface GroupedApplicationItemView extends BaseMainItemView {

    }
}
