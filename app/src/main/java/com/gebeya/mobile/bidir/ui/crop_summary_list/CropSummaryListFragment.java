package com.gebeya.mobile.bidir.ui.crop_summary_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.crop_summary.CropSummaryActivity;
import com.gebeya.mobile.bidir.ui.crop_summary.CropSummaryCashFlowAdapter;

/**
 * Created by Samuel K. on 8/25/2018.
 * <p>
 * samkura47@gmail.com
 */

public class CropSummaryListFragment extends BaseFragment implements CropSummaryListContract.View{

    private CropSummaryListContract.Presenter presenter;

    public static CropSummaryListFragment newInstance(@NonNull String clientId, @NonNull String cropId) {
        final CropSummaryListFragment fragment = new CropSummaryListFragment();

        final Bundle args = new Bundle();
        args.putString(CropSummaryActivity.ARG_CLIENT_ID, clientId);
        args.putString(CropSummaryActivity.ARG_CROP_ID, cropId);
        fragment.setArguments(args);

        return fragment;
    }

    private TextView inputEstimatedValueLabel;
    private TextView inputActualValueLabel;

    private TextView labourCostEstimatedValueLabel;
    private TextView labourCostActualValueLabel;

    private TextView otherCostEstimatedValueLabel;
    private TextView otherCostActualValueLabel;

    private TextView totalEstimatedValueLabel;
    private TextView totalActualValueLabel;

    private TextView probableYieldEstimatedValueLabel;
    private TextView probableYieldActualValueLabel;

    private TextView maximumYieldEstimatedValueLabel;
    private TextView maximumYieldActualValueLabel;

    private TextView minimumYieldEstimatedValueLabel;
    private TextView minimumYieldActualValueLabel;

    private TextView ownConsEstimatedValueLabel;
    private TextView ownConsActualValueLabel;

    private TextView seedResEstimatedValueLabel;
    private TextView seedResActualValueLabel;

    private TextView forMarketEstimatedValueLabel;
    private TextView forMarketActualValueLabel;

    private RecyclerView recyclerView;
    private CropSummaryCashFlowAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args in null.");

        final String clientId = args.getString(CropSummaryActivity.ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client ID is null.");

        final String cropId = args.getString(CropSummaryActivity.ARG_CROP_ID);
        if (cropId == null) throw new NullPointerException("Crop ID is null.");

        presenter = new CropSummaryListPresenter(clientId, cropId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_crop_summary_list_layout, container, false);

        initViews(inflater);

        return root;
    }

    private void initViews(LayoutInflater inflater) {
        inputEstimatedValueLabel = getTv(R.id.cropSummaryInputsEstimatedValueLabel);
        inputActualValueLabel = getTv(R.id.cropSummaryInputsActualValueLabel);

        labourCostEstimatedValueLabel = getTv(R.id.cropSummaryLabourCostEstimatedValueLabel);
        labourCostActualValueLabel = getTv(R.id.cropSummaryLabourCostActualValueLabel);

        otherCostEstimatedValueLabel = getTv(R.id.cropSummaryOtherCostEstimatedValueLabel);
        otherCostActualValueLabel = getTv(R.id.cropSummaryOtherCostActualValueLabel);

        totalEstimatedValueLabel = getTv(R.id.cropSummaryTotalEstimatedValueLabel);
        totalActualValueLabel = getTv(R.id.cropSummarTotalyActualValueLabel);

        probableYieldEstimatedValueLabel = getTv(R.id.cropSummaryProbableYieldEstimatedValueLabel);
        probableYieldActualValueLabel = getTv(R.id.cropSummaryProbableYieldActualValueLabel);

        maximumYieldEstimatedValueLabel = getTv(R.id.cropSummaryMaximumYieldEstimatedValueLabel);
        maximumYieldActualValueLabel = getTv(R.id.cropSummaryMaximumYieldActualValueLabel);

        minimumYieldEstimatedValueLabel = getTv(R.id.cropSummaryMinimumYieldEstimatedValueLabel);
        minimumYieldActualValueLabel = getTv(R.id.cropSummaryMinimumYieldActualValueLabel);

        ownConsEstimatedValueLabel = getTv(R.id.cropSummaryOwnConsEstimatedValueLabel);
        ownConsActualValueLabel = getTv(R.id.cropSummaryOwnConsActualValueLabel);

        seedResEstimatedValueLabel = getTv(R.id.cropSummarySeedResEstimatedValueLabel);
        seedResActualValueLabel = getTv(R.id.cropSummarySeedResActualValueLabel);

        forMarketEstimatedValueLabel = getTv(R.id.cropSummaryForMarketEstimatedValueLabel);
        forMarketActualValueLabel = getTv(R.id.cropSummaryForMarketActualValueLabel);

        recyclerView = getView(R.id.cropSummaryNetCashFlowRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParent(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getParent(), LinearLayoutManager.HORIZONTAL));
        adapter = new CropSummaryCashFlowAdapter(presenter, inflater);
    }

    @Override
    public void setInputs(@NonNull String estimated, @NonNull String actual) {
        inputEstimatedValueLabel.setText(estimated);
        inputActualValueLabel.setText(actual);
    }

    @Override
    public void setLabourCosts(@NonNull String estimated, @NonNull String actual) {
        labourCostEstimatedValueLabel.setText(estimated);
        labourCostActualValueLabel.setText(actual);
    }

    @Override
    public void setOtherCosts(@NonNull String estimated, @NonNull String actual) {
        otherCostEstimatedValueLabel.setText(estimated);
        otherCostActualValueLabel.setText(actual);
    }

    @Override
    public void setTotal(@NonNull String estimated, @NonNull String actual) {
        totalEstimatedValueLabel.setText(estimated);
        totalActualValueLabel.setText(actual);
    }

    @Override
    public void setProbableYield(double value, boolean estimated) {
        (estimated ? probableYieldEstimatedValueLabel : probableYieldActualValueLabel).setText(String.valueOf(value));
    }

    @Override
    public void setMaximumYield(double value, boolean estimated) {
        (estimated ? maximumYieldEstimatedValueLabel : maximumYieldActualValueLabel).setText(String.valueOf(value));
    }

    @Override
    public void setMinimumYield(double value, boolean estimated) {
        (estimated ? minimumYieldEstimatedValueLabel : minimumYieldActualValueLabel).setText(String.valueOf(value));
    }


    @Override
    public void setOwnCons(double value, boolean estimated) {
        (estimated ? ownConsEstimatedValueLabel : ownConsActualValueLabel).setText(String.valueOf(value));
    }

    @Override
    public void setSeedRes(double value, boolean estimated) {
        (estimated ? seedResEstimatedValueLabel : seedResActualValueLabel).setText(String.valueOf(value));
    }

    @Override
    public void setForMarket(double value, boolean estimated) {
        (estimated ? forMarketEstimatedValueLabel : forMarketActualValueLabel).setText(String.valueOf(value));
    }

    @Override
    public void loadCashFlowData() {
        recyclerView.setAdapter(adapter);
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
    public void onStop() {
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void attachPresenter(CropSummaryListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getParent().finish();
    }
}
