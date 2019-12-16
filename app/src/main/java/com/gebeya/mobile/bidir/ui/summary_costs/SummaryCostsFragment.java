package com.gebeya.mobile.bidir.ui.summary_costs;

import android.content.Intent;
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
import com.gebeya.mobile.bidir.data.acatcostsection.ACATCostSection;
import com.gebeya.mobile.bidir.data.cropacat.CropACAT;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryCashFlowAdapter;
import com.gebeya.mobile.bidir.ui.summary.SummaryMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * View implementation for the {@link SummaryCostsContract.View} view as a Fragment.
 */
public class SummaryCostsFragment extends BaseFragment implements SummaryCostsContract.View {

    private static final String ARG_LABEL = "LABEL";
    private Bundle bundle = new Bundle();

    public static SummaryCostsFragment newInstance(@NonNull SummaryMenuItem.Label label,
                                                   @NonNull String clientId,
                                                   @NonNull String cropACATId) {
        final SummaryCostsFragment fragment = new SummaryCostsFragment();
        final Bundle args = new Bundle();

        args.putSerializable(ARG_LABEL, label);
        args.putString(SummaryActivity.ARG_CLIENT_ID, clientId);
        args.putString(SummaryActivity.ARG_CROP_ACAT_ID, cropACATId);
        fragment.setArguments(args);

        return fragment;
    }

    private SummaryCostsContract.Presenter presenter;

    private TextView titleLabel;

    private RecyclerView estimatedRecyclerView;
    private SummaryEstimatedCostsAdapter estimatedAdapter;

    private RecyclerView actualRecyclerView;
    private SummaryActualCostsAdapter actualAdapter;

    private RecyclerView cashFlowRecyclerView;
    private SummaryCashFlowAdapter cashFlowAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();
        if (args == null) throw new NullPointerException("Bundle args is null");

        final SummaryMenuItem.Label label = (SummaryMenuItem.Label) args.getSerializable(ARG_LABEL);
        if (label == null) throw new NullPointerException("SummaryMenuItem.Label is null");

        final String clientId = args.getString(SummaryActivity.ARG_CLIENT_ID);
        if (clientId == null) throw new NullPointerException("Client ID is null");

        final String cropACATId = args.getString(SummaryActivity.ARG_CROP_ACAT_ID);
        if (cropACATId == null) throw new NullPointerException("Crop ID is null");

        presenter = new SummaryCostsPresenter(label, clientId, cropACATId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_summary_costs, container, false);

        titleLabel = getTv(R.id.summaryCostsTitleLabel);

        getView(R.id.monitorNewFab).setOnClickListener(fabClickListener);

        estimatedRecyclerView = getView(R.id.summaryCostsEstimatedRecyclerView);
        estimatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        estimatedAdapter = new SummaryEstimatedCostsAdapter(presenter, inflater);

        actualRecyclerView = getView(R.id.summaryCostsActualRecyclerView);
        actualRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        actualAdapter = new SummaryActualCostsAdapter( presenter, inflater);

        cashFlowRecyclerView = getView(R.id.summaryCostsCashFlowRecyclerView);
        cashFlowRecyclerView.setLayoutManager(new LinearLayoutManager(getParent(), LinearLayoutManager.HORIZONTAL, false));
        cashFlowRecyclerView.addItemDecoration(new DividerItemDecoration(getParent(), LinearLayoutManager.HORIZONTAL));
        cashFlowAdapter = new SummaryCashFlowAdapter(presenter, inflater);

        return root;
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onMonitorButtonClicked(bundle);
        }
    };

    @Override
    public void setTitle(@NonNull SummaryMenuItem.Label label) {
        final String title = getTitle(label);
        if (title == null) return;

        titleLabel.setText(title);
    }

    @Nullable
    public String getTitle(@NonNull SummaryMenuItem.Label label) {
        switch (label) {
            case LABOR_COST:
                return getString(R.string.summary_menu_labor_costs_label);
            case OTHER_COSTS:
                return getString(R.string.summary_menu_other_costs_label);
            case SEEDS:
                return getString(R.string.summary_menu_seeds_label);
            case CHEMICALS:
                return getString(R.string.summary_menu_chemicals_label);
            case FERTILIZERS:
                return getString(R.string.summary_menu_fertilizers_label);
        }
        return null;
    }

    @Override
    public void loadEstimatedData() {
        estimatedRecyclerView.setAdapter(estimatedAdapter);
    }

    @Override
    public void loadActualData() {
        actualRecyclerView.setAdapter(actualAdapter);
    }

    @Override
    public void loadCashFlowData() {
        cashFlowRecyclerView.setAdapter(cashFlowAdapter);
    }

    @Override
    public void openFragment(@NonNull Bundle arg,
                             @NonNull String clientId,
                             @NonNull String cropACATId,
                             @NonNull SummaryMenuItem.Label label,
                             @NonNull List<ACATCostSection> costSections,
                             @NonNull CropACAT cropACAT,
                             @NonNull String sectionId) {
        ArrayList<String> titleLabels = new ArrayList<>();
        titleLabels.clear();
        titleLabels.add(cropACAT.cropName);
        titleLabels.add(getTitle(label));
        final Intent intent = new Intent(getActivity(), ACATCostEstimationActivity.class);

        intent.putExtra(ACATCostEstimationActivity.ARG_CROP_ACAT_ID, cropACATId)
                .putExtra(ACATCostEstimationActivity.ARG_CLIENT_ID, clientId)
                .putExtra(ACATCostEstimationActivity.IS_MONITORING, true)
                .putExtra(ACATCostEstimationActivity.SECTION_ID, sectionId)
                .putExtra(ACATCostEstimationActivity.SUBTOTAL_LABEL, getTitle(label))
                .putStringArrayListExtra(ACATCostEstimationActivity.TITLE_LABEL, titleLabels);

        startActivity(intent);
        getActivity().finish();
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
    public void attachPresenter(SummaryCostsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void close() {
        getParent().finish();
    }
}