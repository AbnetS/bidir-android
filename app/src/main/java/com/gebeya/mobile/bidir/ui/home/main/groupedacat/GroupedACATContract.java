package com.gebeya.mobile.bidir.ui.home.main.groupedacat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;

public interface GroupedACATContract {

    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull GroupedACATsItemView holder, int position);
        void onACATSelected(int position);
        int getACATCount();

        void onDoneClicked();

        void onLoadingComplete();
        void onLoadingFailed(@NonNull Throwable throwable);
        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

        void onBackButtonPressed();

        void onOptionMenuClicked(@NonNull TextView anchor, int position);
    }

    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull String title);
        void showACATs();
        void showLoadingProgress();
        void hideLoadingProgress();
        void showUpdateProgress();
        void hideUpdateProgress();
        void showError(@NonNull Result result);
        void showUpdateSuccessfulMessage();

        void toggleDoneMenuButton(boolean show);

        void openACAT(@NonNull String clientId, @Nullable String groupId);
        void openACATApplicationInitializer(@NonNull String clientId, @Nullable String groupId);
        void openCropList(@NonNull String clientId, @Nullable String groupId);

        void openPopUpMenu(@NonNull TextView anchor, @NonNull Client client, @NonNull Group group);
    }

    interface GroupedACATsItemView extends BaseMainItemView {

    }
}
