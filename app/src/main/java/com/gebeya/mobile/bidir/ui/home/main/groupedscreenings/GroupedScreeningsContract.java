package com.gebeya.mobile.bidir.ui.home.main.groupedscreenings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;

public interface GroupedScreeningsContract {

    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull GroupedScreeningsItemView holder, int position);
        void onScreeningSelected(int position);
        int getScreeningCount();

        void onDoneClicked();

        void onLoadingComplete();
        void onLoadingFailed(@NonNull Throwable throwable);
        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

        void onLoanCreated();

        void onBackButtonPressed();
    }

    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull String title);
        void showScreenings();
        void showLoadingProgress();
        void hideLoadingProgress();
        void showUpdateProgress();
        void hideUpdateProgress();
        void showError(@NonNull Result result);
        void showUpdateSuccessfulMessage();

        void openScreening(@NonNull String screeningId, @Nullable String groupId);

        void showCreatingLoanProgress();
    }

    interface GroupedScreeningsItemView extends BaseMainItemView {

    }
}
