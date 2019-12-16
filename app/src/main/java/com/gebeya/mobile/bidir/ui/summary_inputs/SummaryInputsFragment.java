package com.gebeya.mobile.bidir.ui.summary_inputs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.summary.SummaryActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryCashFlowAdapter;

/**
 * {@link SummaryInputsContract.View} view implementation as a Fragment.
 */
public class SummaryInputsFragment extends BaseFragment implements SummaryInputsContract.View {

    private SummaryInputsContract.Presenter presenter;

    public static SummaryInputsFragment newInstance(@NonNull String clientId, @NonNull String cropACATId) {
        final SummaryInputsFragment fragment = new SummaryInputsFragment();

        final Bundle args = new Bundle();
        args.putString(SummaryActivity.ARG_CLIENT_ID, clientId);
        args.putString(SummaryActivity.ARG_CROP_ACAT_ID, cropACATId);
        fragment.setArguments(args);

        return fragment;
    }


    private TextView seedEstimatedValueLabel;
    private TextView seedActualValueLabel;

    private TextView fertilizerEstimatedValueLabel;
    private TextView fertilizerActualValueLabel;

    private TextView chemicalsEstimatedValueLabel;
    private TextView chemicalsActualValueLabel;

    private TextView subtotalEstimatedValueLabel;
    private TextView subtotalActualValueLabel;

    private RecyclerView recyclerView;
    private SummaryCashFlowAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null");

        final String clientId = args.getString(SummaryActivity.ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client ID is null");

        final String cropACATId = args.getString(SummaryActivity.ARG_CROP_ACAT_ID);
        if (cropACATId == null) throw new NullPointerException("Crop ID is null");

        presenter = new SummaryInputsPresenter(clientId, cropACATId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_summary_inputs, container, false);

        seedEstimatedValueLabel = getTv(R.id.summaryInputsSeedEstimatedValueLabel);
        seedActualValueLabel = getTv(R.id.summaryInputsSeedActualValueLabel);

        fertilizerEstimatedValueLabel = getTv(R.id.summaryInputsEstimatedFertilizerValueLabel);
        fertilizerActualValueLabel = getTv(R.id.summaryInputsActualFertilizerValueLabel);

        chemicalsEstimatedValueLabel = getTv(R.id.summaryInputsEstimatedChemicalsValueLabel);
        chemicalsActualValueLabel = getTv(R.id.summaryInputsActualChemicalsValueLabel);

        subtotalEstimatedValueLabel = getTv(R.id.summaryInputsEstimatedSubtotalValueLabel);
        subtotalActualValueLabel = getTv(R.id.summaryInputsActualSubtotalValueLabel);

        recyclerView = getView(R.id.summaryInputsCashFlowRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParent(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getParent(), LinearLayoutManager.HORIZONTAL));
        adapter = new SummaryCashFlowAdapter(presenter, inflater);

        return root;
    }

    @Override
    public void setSeedData(@NonNull String estimated, @NonNull String actual) {
        seedEstimatedValueLabel.setText(estimated);
        seedActualValueLabel.setText(actual);
    }

    @Override
    public void setFertilizerData(@NonNull String estimated, @NonNull String actual) {
        fertilizerEstimatedValueLabel.setText(estimated);
        fertilizerActualValueLabel.setText(actual);
    }

    @Override
    public void setChemicalsData(@NonNull String estimated, @NonNull String actual) {
        chemicalsEstimatedValueLabel.setText(estimated);
        chemicalsActualValueLabel.setText(actual);
    }

    @Override
    public void setSubtotalData(@NonNull String estimated, @NonNull String actual) {
        subtotalEstimatedValueLabel.setText(estimated);
        subtotalActualValueLabel.setText(actual);
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
    public void attachPresenter(SummaryInputsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getParent().finish();
    }
}
