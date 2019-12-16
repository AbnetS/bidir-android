package com.gebeya.mobile.bidir.ui.home.main.clientlists;


import android.support.annotation.NonNull;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;

public interface ClientListContract {

    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull ClientItemView holder, int position);
        void onOKButtonClicked();
        void onClientSelected(int position);
        int getClientCount();

        void onUpdateMemberComplete();
        void onUpdateComplete();
        void onUpdateFailed(Throwable throwable);
        void onBackButtonPressed();
    }

    interface View extends BaseView<Presenter> {

        void showClients();
        void toggleNoClientsLabel(boolean show);
        void updateSelectedClientCount(int size);
        void refreshClients();

        void hideUpdateProgress();
        void showError(@NonNull Result result);

        void setToolbarTitle();
        void showUpdateSuccessMessage();

        void shoUpdateProgress();
    }

    interface ClientItemView extends BaseMainItemView {
        void setChoice(boolean checked);
    }
}
