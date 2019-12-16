package com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.common.dialogs.acatcroprotation.CropRotationCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.acatcroprotation.CropRotationDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.formarket.ForMarketCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.formarket.ForMarketDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.form.questions.values.InputWatcher;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem.ACATCropItemActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatloanproposal.ACATLoanProposalActivity;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */
public class ACATEstimatedYieldFragment extends BaseFragment implements ACATEstimatedYieldContract.View, CropRotationCallback,
        ErrorDialogCallback,
        ForMarketCallback{

    private ACATEstimatedYieldContract.Presenter presenter;

    private Toolbar toolbar;

    private LinearLayout yieldListContainer;

    private String union;
    private String factory;
    private String trader;
    private String unionRemark;
    private String factoryRemark;
    private String traderRemark;
    private boolean isExpected;

    private RecyclerView yieldCashOutflowRecyclerView;
    private ACATEstimatedYieldCashOutflowAdapter yieldCashOutflowAdapter;
    private Button nextButton;
    private Button previousButton;

    private View progressContainer;
    private View yieldCashFlowContainer;
    private View yieldIndicatorContainer;

    private EditText estimatedUnitInput;
    private EditText estimatedQuantityInput;
    private EditText estimatedUnitPriceInput;
    private TextView estimatedTotalLabel;
    private View estimatedYieldConsumptionInputsContainer;
    private EditText estimatedOwnConsInput;
    private EditText estimatedSeedResInput;
    private EditText estimatedForMarketInput;
    private EditText yieldRemarkInput;

    private EditText actualUnitInput;
    private EditText actualQuantityInput;
    private EditText actualUnitPriceInput;
    private TextView actualTotalLabel;
    private TextView cropLabel;
    private View actualYieldConsumptionInputsContainer;
    private EditText actualOwnConsInput;
    private EditText actualSeedResInput;
    private EditText actualForMarketInput;
    private EditText yieldConsumptionRemarkInput;
    private Button marketDetailsButton;

    private View actualRemarkContainer;

    private TextView probableRevenueLabel;
    private TextView maxRevenueLabel;
    private TextView minRevenueLabel;

    private RecyclerView netCashFlowRecyclerView;
    private ACATEstimatedNetCashOutflowAdapter netCashFlowAdapter;

    CropRotationDialog rotationDialog;
    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    private ForMarketDialog forMarketDialog;


    private int activePosition;

    public static ACATEstimatedYieldFragment newInstance() {
        return new ACATEstimatedYieldFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_acat_estimated_yield_layout, container, false);

        toolbar = getView(R.id.acat_estimated_yield_toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_icon);

        nextButton = getBt(R.id.acat_estimated_yield_next_step_button);
        nextButton.setOnClickListener(v -> {
            ACATEstimatedYieldDto dto = gatherData();
            try {
                presenter.onNextClicked(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        cropLabel = getTv(R.id.acat_estimated_yield_crop_item_label);
        previousButton = getBt(R.id.acat_estimated_yield_previous_step_button);
        previousButton.setOnClickListener(v -> {
            ACATEstimatedYieldDto dto = gatherData();
            try {
                presenter.onPreviousClicked(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());

        yieldListContainer = getView(R.id.data_entry_item_layout);
        yieldIndicatorContainer = getView(R.id.revenueYieldInputIndicatorsContainer);
        initializeYieldListContainer();
        initializeRevenueInputs(presenter.getMonitoring());

        probableRevenueLabel = getTv(R.id.loan_assessment_probable_revenue_label);
        maxRevenueLabel = getTv(R.id.loan_assessment_maximum_revenue_label);
        minRevenueLabel = getTv(R.id.loan_assessment_minimum_revenue_label);

//        marketDetailsButton = getBt(R.id.revenueYieldMarketDetailsButton);
//        marketDetailsButton.setOnClickListener(view -> presenter.onDetailButtonClicked());

        yieldCashOutflowRecyclerView = getView(R.id.cash_out_flow_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        yieldCashOutflowRecyclerView.setLayoutManager(manager);
        yieldCashOutflowAdapter = new ACATEstimatedYieldCashOutflowAdapter(inflater, presenter);

        netCashFlowRecyclerView = getView(R.id.acat_net_cash_flow_recycler_view);

        LinearLayoutManager netCashFlowManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        netCashFlowRecyclerView.setLayoutManager(netCashFlowManager);
        netCashFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));

        netCashFlowAdapter = new ACATEstimatedNetCashOutflowAdapter(inflater, presenter);
        progressContainer = getView(R.id.revenueProbableExpectedVsAchievedProgressContainer);
        yieldCashFlowContainer = getView(R.id.revenueProbableCashFlowContainer);

        return root;
    }


    @Override
    public void openLoanProposal(@NonNull String clientId, @NonNull String acatApplicationId) {
        final Intent intent = new Intent(getActivity(), ACATLoanProposalActivity.class);
        intent.putExtra(ACATLoanProposalActivity.CLIENT_ID, clientId);
        intent.putExtra(ACATLoanProposalActivity.ACAT_ID, acatApplicationId);
        startActivity(intent);
    }

    @Override
    public void openCropRotationDialog(@NonNull String previousCropName, @NonNull String nextCropName) {
        rotationDialog = CropRotationDialog.newInstance(previousCropName, nextCropName);
        rotationDialog.setCallback(this);
        rotationDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void openCropList(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), ACATCropItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ACATCropItemActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
    }

    @Override
    public void openACATCostEstimation(@NonNull String clientId) {
        final Intent intent = new Intent(getActivity(), ACATCostEstimationActivity.class);
        intent.putExtra(ACATCostEstimationActivity.ARG_CLIENT_ID, clientId);
        startActivity(intent);
    }

    @Override
    public void openPreviousScreen() {
        getActivity().onBackPressed();
    }

    @Override
    public void toggleProgress(boolean show) {
        progressContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        yieldCashFlowContainer.setVisibility(!show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void onRotationCallBack() {
        presenter.onRotationCallBackReturned();
    }

    @Override
    public void setCropLabel(@NonNull String cropLabel) {
        this.cropLabel.setText(cropLabel);
    }

    private void initializeYieldListContainer() {
        final int length = yieldListContainer.getChildCount();
        for (int i = 0; i < length; i++) {
            final View yieldItem = yieldListContainer.getChildAt(i);
            yieldItem.setOnClickListener(yieldItemListener);
        }
    }

    private final View.OnClickListener yieldItemListener = v -> {
        final int position = yieldListContainer.indexOfChild(v);
        final ACATEstimatedYieldDto dto = gatherData();
        presenter.onYieldSelected(position, dto);
    };

    @Override
    public void highlightYieldTitle(int position) {
        activePosition = position;
        final int length = yieldListContainer.getChildCount();
        for (int i = 0; i < length; i++) {
            final View yieldItem = yieldListContainer.getChildAt(i);
            final int background = i == position ? R.drawable.acat_item_selected_green : R.drawable.acat_green_custom_border;
            yieldItem.setBackgroundResource(background);
        }
    }

    @Override
    public void loadYieldCashOutflow() {
        yieldCashOutflowRecyclerView.setAdapter(yieldCashOutflowAdapter);
    }

    @Override
    public void loadNetCashOutflow() {
        netCashFlowRecyclerView.setAdapter(netCashFlowAdapter);
    }

    @Override
    public void refreshNetCashOutflow() {
        netCashFlowAdapter.notifyDataSetChanged();
    }

    private void initializeRevenueInputs(boolean isMonitoring) {
        estimatedUnitInput = getEd(R.id.revenueEstimatedUnitInput);
        estimatedQuantityInput = getEd(R.id.revenueEstimatedQuantityInput);
        estimatedQuantityInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(true);
            }
        });
        estimatedUnitPriceInput = getEd(R.id.revenueEstimatedUnitPriceInput);
        estimatedUnitPriceInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(true);
            }
        });
        estimatedTotalLabel = getTv(R.id.revenueEstimatedTotalLabel);
        estimatedYieldConsumptionInputsContainer = getView(R.id.revenueEstimatedYieldConsumptionInputsContainer);
        estimatedOwnConsInput = getEd(R.id.revenueEstimatedOwnConsInput);
        estimatedSeedResInput = getEd(R.id.revenueEstimatedSeedResInput);
        estimatedForMarketInput = getEd(R.id.revenueEstimatedForMarketInput);
        estimatedForMarketInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshMarkettableRevenue(true);
            }
        });

        estimatedForMarketInput.setEnabled(isMonitoring);

        yieldRemarkInput = getEd(R.id.revenueYieldRemarkInput);

        actualUnitInput = getEd(R.id.revenueActualUnitInput);
        actualUnitInput.setEnabled(isMonitoring);

        actualQuantityInput = getEd(R.id.revenueActualQuantityInput);
        actualQuantityInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(true);
            }
        });

        actualQuantityInput.setEnabled(isMonitoring);

        actualUnitPriceInput = getEd(R.id.revenueActualUnitPriceInput);
        actualUnitPriceInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTotal(true);
            }
        });

        actualUnitPriceInput.setEnabled(isMonitoring);

        actualTotalLabel = getTv(R.id.revenueActualTotalLabel);
        actualYieldConsumptionInputsContainer = getView(R.id.revenueActualYieldConsumptionInputsContainer);
        actualOwnConsInput = getEd(R.id.revenueActualOwnConsInput);
        actualOwnConsInput.setEnabled(isMonitoring);
        actualSeedResInput = getEd(R.id.revenueActualSeedResInput);
        actualSeedResInput.setEnabled(isMonitoring);
        actualForMarketInput = getEd(R.id.revenueActualForMarketInput);

        actualForMarketInput.setEnabled(isMonitoring);
        yieldConsumptionRemarkInput = getEd(R.id.revenueYieldConsumptionRemarkInput);
        yieldConsumptionRemarkInput.addTextChangedListener(new InputWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        actualRemarkContainer = getView(R.id.item_expense_value_container);
    }

    @Override
    public void setUnit(@NonNull String unit, boolean estimated) {
        (estimated ? estimatedUnitInput : actualUnitInput).setText(unit);
    }

    @Override
    public void setQuantity(double quantity, boolean estimated) {
        (estimated ? estimatedQuantityInput : actualQuantityInput).setText(String.valueOf(quantity));
    }

    @Override
    public void setUnitPrice(double unitPrice, boolean estimated) {
        (estimated ? estimatedUnitPriceInput : actualUnitPriceInput).setText(String.valueOf(unitPrice));
    }

    @Override
    public void setTotal(double total, boolean estimated) {
        (estimated ? estimatedTotalLabel : actualTotalLabel).setText(String.valueOf(total));
    }

    @Override
    public void setOwnCons(double value, boolean estimated) {
        (estimated ? estimatedOwnConsInput : actualOwnConsInput).setText(String.valueOf(value));
    }

    @Override
    public void setSeedRes(double value, boolean estimated) {
        (estimated ? estimatedSeedResInput : actualSeedResInput).setText(String.valueOf(value));
    }

    @Override
    public void setForMarket(double value, boolean estimated) {
        (estimated ? estimatedForMarketInput : actualForMarketInput).setText(String.valueOf(value));
    }

    @Override
    public void setYieldRemark(@NonNull String remark) {
        yieldRemarkInput.setText(remark);
    }

    @Override
    public void setYieldConsumptionRemark(@NonNull String remark) {
        yieldConsumptionRemarkInput.setText(remark);
    }

    @Override
    public void showMissingUnitError(boolean estimated) {
        final EditText target = (estimated ? estimatedUnitInput : actualUnitInput);
        target.setError(getString(R.string.error_missing_unit_error));
        target.requestFocus();
    }

    @Override
    public void showMissingQuantityError(boolean estimated) {
        final EditText target = (estimated ? estimatedQuantityInput: actualQuantityInput);
        target.setError(getString(R.string.error_missing_quantity_error));
        target.requestFocus();
    }

    @Override
    public void showInvalidQuantityError(boolean estimated) {
        final EditText target = (estimated ? estimatedQuantityInput: actualQuantityInput);
        target.setError(getString(R.string.error_invalid_quantity_error));
        target.requestFocus();
    }

    @Override
    public void showMissingUnitPriceError(boolean estimated) {
        final EditText target = (estimated ? estimatedUnitPriceInput: actualUnitPriceInput);
        target.setError(getString(R.string.error_missing_unit_price_error));
        target.requestFocus();
    }

    @Override
    public void showInvalidUnitPriceError(boolean estimated) {
        final EditText target = (estimated ? estimatedUnitPriceInput: actualUnitPriceInput);
        target.setError(getString(R.string.error_invalid_unit_price_error));
        target.requestFocus();
    }

    @Override
    public void showMissingOwnConsError(boolean estimated) {
        final EditText target = (estimated ? estimatedOwnConsInput : actualOwnConsInput);
        target.setError(getString(R.string.error_missing_own_cons_error));
        target.requestFocus();
    }

    @Override
    public void showMissingSeedResError(boolean estimated) {
        final EditText target = (estimated ? estimatedSeedResInput : actualSeedResInput);
        target.setError(getString(R.string.error_missing_seed_res_error));
        target.requestFocus();
    }

    @Override
    public void showMissingForMarketError(boolean estimated) {
        final EditText target = (estimated ? estimatedForMarketInput : actualForMarketInput);
        target.setError(getString(R.string.error_missing_for_market_error));
        target.requestFocus();
    }

    @Override
    public void showInvalidYieldConsumptionInputsError(boolean estimated) {
        final String error = getString(R.string.error_invalid_yield_consumption_quantity_error);

        (estimated ? estimatedQuantityInput: actualQuantityInput).setError(error);
        (estimated ? estimatedOwnConsInput : actualOwnConsInput).setError(error);
        (estimated ? estimatedSeedResInput : actualSeedResInput).setError(error);
        (estimated ? estimatedForMarketInput : actualForMarketInput).setError(error);
    }

    public ACATEstimatedYieldDto gatherData() {
        final ACATEstimatedYieldDto dto = new ACATEstimatedYieldDto();

        dto.estimatedUnit = getText(estimatedUnitInput);
        dto.estimatedQuantity = getText(estimatedQuantityInput);
        dto.estimatedUnitPrice = getText(estimatedUnitPriceInput);
        dto.estimatedOwnCons = getText(estimatedOwnConsInput);
        dto.estimatedSeedRes = getText(estimatedSeedResInput);
        dto.estimatedForMarket = getText(estimatedForMarketInput);
        dto.yieldRemark = getText(yieldRemarkInput);

        dto.actualUnit = getText(actualUnitInput);
        dto.actualQuantity = getText(actualQuantityInput);
        dto.actualUnitPrice = getText(actualUnitPriceInput);
        dto.actualOwnCons = getText(actualOwnConsInput);
        dto.actualSeedRes = getText(actualSeedResInput);
        dto.actualForMarket = getText(actualForMarketInput);
        dto.yieldConsumptionRemark = getText(yieldConsumptionRemarkInput);

        return dto;
    }

    private void refreshTotal(boolean estimated) {
        String quantityString = (estimated ? estimatedQuantityInput : actualQuantityInput).getText().toString().trim();
        if (quantityString.isEmpty()) {
            quantityString = "0";
        }
        final double quantity = Double.parseDouble(quantityString);

        String unitPriceString = (estimated ? estimatedUnitPriceInput : actualUnitPriceInput).getText().toString().trim();
        if (unitPriceString.isEmpty()) {
            unitPriceString = "0";
        }
        final double unitPrice = Double.parseDouble(unitPriceString);

        final double total = quantity * unitPrice;
        setTotal(total, estimated);

        setRespectiveRevenue(Double.toString(total), activePosition);
    }

    private void refreshMarkettableRevenue(boolean estimated) {
        String quantityString = (estimated ? estimatedForMarketInput : actualForMarketInput).getText().toString().trim();
        if (quantityString.isEmpty()) {
            quantityString = "0";
        }
        final double quantity = Double.parseDouble(quantityString);

        String unitPriceString = (estimated ? estimatedUnitPriceInput : actualUnitPriceInput).getText().toString().trim();
        if (unitPriceString.isEmpty()) {
            unitPriceString = "0";
        }
        final double unitPrice = Double.parseDouble(unitPriceString);

        final double total = quantity * unitPrice;
        setProbableRevenue(Double.toString(total));
    }

    @NonNull
    private String getText(@NonNull EditText input) {
        return input.getText().toString().trim();
    }

    @Override
    public void toggleYieldConsumptionInputs(boolean show) {
        final int visibility = show ? View.VISIBLE : View.GONE;
        estimatedYieldConsumptionInputsContainer.setVisibility(visibility);
        actualYieldConsumptionInputsContainer.setVisibility(visibility);
        yieldIndicatorContainer.setVisibility(visibility);
        actualRemarkContainer.setVisibility(visibility);
//        marketDetailsButton.setVisibility(visibility);
    }

    @Override
    public void setRespectiveRevenue(@NonNull String value, @NonNull int activePosition){
        if (activePosition == 1) setMaximumRevenue(value);
        else if (activePosition == 2) setMinimumRevenue(value);
    }

    @Override
    public void setProbableRevenue(@NonNull String value) {
        probableRevenueLabel.setText(value);
    }

    @Override
    public void setMaximumRevenue(@NonNull String value) {
        maxRevenueLabel.setText(value);
    }

    @Override
    public void setMinimumRevenue(@NonNull String value) {
        minRevenueLabel.setText(value);
    }

    @Override
    public void showUpdatingMessage() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.updating_acat_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideUpdatingMessage() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;

        if (message == null) {
            message = getString(R.string.acat_questions_error_generic);
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(
                getString(R.string.questions_error_title),
                message,
                extra
        );

        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void openForMarketDialog() {
        forMarketDialog = ForMarketDialog.
                newInstance(union,
                            factory,
                            trader,
                            unionRemark,
                            factoryRemark,
                            traderRemark,
                            isExpected);
        forMarketDialog.setCallback(this);

        forMarketDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void attachPresenter(ACATEstimatedYieldContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onPause() {
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void close() {
        getParent().finish();
    }

    @Override
    public void onErrorDialogDismissed() {
        close();
    }

    @Override
    public void forMarketUnionCallback(@Nullable String union, @Nullable String unionRemark, boolean isExpected) {

    }

    @Override
    public void forMarketFactoryCallback(@Nullable String factoryEstimated, @Nullable String unionRemark, boolean isExpected) {

    }

    @Override
    public void forMarketTraderCallback(@Nullable String traderEstimated, @Nullable String traderRemark, boolean isExpected) {

    }
}
