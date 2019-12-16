package com.gebeya.mobile.bidir.ui.home.main.groupedloans;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.ui.home.main.BaseMainItemView;

public interface GroupedLoansContract {

    interface Presenter extends BasePresenter<View> {
        void onBindRowView(@NonNull GroupedLoansItemView holder, int position);
        void onLoanSelected(int position);
        int getLoanCount();

        void onDoneClicked();

        void onLoadingComplete();
        void onLoadingFailed(@NonNull Throwable throwable);
        void onUpdateComplete();
        void onUpdateFailed(@NonNull Throwable throwable);

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

        void openLoan(@NonNull String screeningId, @Nullable String groupId);

        void showCreatingACATProgress();
    }

    interface GroupedLoansItemView extends BaseMainItemView {

    }
}
