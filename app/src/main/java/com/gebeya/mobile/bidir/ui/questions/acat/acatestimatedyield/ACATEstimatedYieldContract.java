package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.base.BasePresenter;
import com.gebeya.apps.framework.base.BaseView;
import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;

/**
 * Interface definition for the ACAT estimated yield component.
 */
public interface ACATEstimatedYieldContract {

    /**
     * View interface definition.
     */
    interface View extends BaseView<Presenter> {
        void setTitle(@NonNull String title);
        void setCropLabel(@NonNull String cropLabel);
        void highlightYieldTitle(int position);

        void loadYieldCashOutflow();
        void loadNetCashOutflow();
        void refreshNetCashOutflow();

        void showMissingUnitError(boolean estimated);
        void showMissingQuantityError(boolean estimated);
        void showInvalidQuantityError(boolean estimated);
        void showMissingUnitPriceError(boolean estimated);
        void showInvalidUnitPriceError(boolean estimated);

        void showMissingOwnConsError(boolean estimated);
        void showMissingSeedResError(boolean estimated);
        void showMissingForMarketError(boolean estimated);
        void showInvalidYieldConsumptionInputsError(boolean estimated);

        void showUpdatingMessage();
        void hideUpdatingMessage();

        void setUnit(@NonNull String unit, boolean estimated);
        void setQuantity(double quantity, boolean estimated);
        void setUnitPrice(double unitPrice, boolean estimated);
        void setTotal(double total, boolean estimated);
        void setOwnCons(double value, boolean estimated);
        void setSeedRes(double value, boolean estimated);
        void setForMarket(double value, boolean estimated);
        void setYieldRemark(@NonNull String remark);
        void setYieldConsumptionRemark(@NonNull String remark);
        void toggleYieldConsumptionInputs(boolean show);

        void setProbableRevenue(@NonNull String value);
        void setMaximumRevenue(@NonNull String value);
        void setMinimumRevenue(@NonNull String value);

        void setRespectiveRevenue(@NonNull String value, @NonNull int activePosition);

        void openLoanProposal(@NonNull String clientId, @NonNull String acatApplicationId);

        void openPreviousScreen();
        void toggleProgress(boolean show);


        void openCropRotationDialog(@NonNull String previousCropName, @NonNull String nextCropName);

        void openCropList(@NonNull String clientId);

        void openACATCostEstimation(@NonNull  String clientId);

        void showError(@NonNull Result error);

        void openForMarketDialog();
    }

    /**
     * Presenter interface definition.
     */
    interface Presenter extends BasePresenter<View> {
        void onBindYieldViewHolder(@NonNull CashFlowItemView holder, int position);
        void onBindNetViewHolder(@NonNull CashFlowItemView holder, int position);
        void onYieldSelected(int position, @NonNull ACATEstimatedYieldDto dto);
        void onCashFlowItemUpdated(@NonNull String value, int position);
        int cashYieldFlowCount();
        int cashNetFlowCount();

        void onNextClicked(@Nullable ACATEstimatedYieldDto dto) throws Exception;
        void onPreviousClicked(@Nullable ACATEstimatedYieldDto dt0) throws Exception;

        void onRotationCallBackReturned();

        void onUpdateComplete(@NonNull CropACAT response);

        void onUpdateFailed(@NonNull Throwable throwable);

        void onDetailButtonClicked();

        boolean getMonitoring();
    }

    /**
     * Item view interface definition.
     */
    interface CashFlowItemView {
        void setTitle(@NonNull String title);
        void setValue(@NonNull String value);
    }
}