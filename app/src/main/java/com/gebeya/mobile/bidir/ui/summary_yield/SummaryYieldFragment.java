package com.gebeya.mobile.bidir.ui.summary_yield;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatestimatedyield.ACATEstimatedYieldActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation.ACATItemCostEstimationFragment;
import com.gebeya.mobile.bidir.ui.questions.acat.estimatedyield.ACATEstimatedRevenueActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryActivity;
import com.gebeya.mobile.bidir.ui.summary.SummaryMenuItem;
import com.gebeya.mobile.bidir.ui.summary.SummaryPresenter;

import static com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation.ACATCostEstimationUtlity.LABOUR_COST;

/**
 * Created by Samuel K. on 8/24/2018.
 * <p>
 * samkura47@gmail.com
 */

public class SummaryYieldFragment extends BaseFragment implements SummaryYieldContract.View {

    private static final String ARG_LABEL = "LABEL";

    private SummaryYieldContract.Presenter presenter;

    public static SummaryYieldFragment newInstance(@NonNull SummaryMenuItem.Label label,
                                                   @NonNull String clientId,
                                                   @NonNull String cropACATId) {
        final SummaryYieldFragment fragment = new SummaryYieldFragment();

        final Bundle args = new Bundle();
        args.putSerializable(ARG_LABEL, label);
        args.putString(SummaryActivity.ARG_CLIENT_ID, clientId);
        args.putString(SummaryActivity.ARG_CROP_ACAT_ID, cropACATId);
        fragment.setArguments(args);

        return fragment;
    }

    private TextView titleLabel;

    private TextView unitEstimatedLabel;
    private TextView unitActualLabel;

    private TextView unitPriceEstimatedLabel;
    private TextView unitPriceActualLabel;

    private TextView totalPriceEstimatedLabel;
    private TextView totalPriceActualLabel;

    private TextView ownConsEstimatedLabel;
    private TextView ownConsActualLabel;

    private TextView seedResEstimatedLabel;
    private TextView seedResActualLabel;

    private TextView forMarketEstimatedLabel;
    private TextView forMarketActualLabel;

    private LinearLayout ownConsContainer;
    private LinearLayout seedResContainer;
    private LinearLayout forMarketContainer;
    private Bundle bundle;
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

        presenter = new SummaryYieldPresenter(label, clientId, cropACATId);

        bundle = new Bundle();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_summary_yields, container, false);

        titleLabel = getTv(R.id.summaryYieldTitleLabel);

        getView(R.id.monitorNewFab).setOnClickListener(fabClickListener);

        unitEstimatedLabel = getTv(R.id.summaryYieldEstimatedUnitValue);
        unitActualLabel = getTv(R.id.summaryYieldActualUnitValue);

        unitPriceEstimatedLabel = getTv(R.id.summaryYieldEstimatedUnitPriceValue);
        unitPriceActualLabel = getTv(R.id.summaryYieldActualUnitPriceValue);

        totalPriceEstimatedLabel = getTv(R.id.summaryYieldEstimatedTotalPriceValue);
        totalPriceActualLabel = getTv(R.id.summaryYieldActualTotalPriceValue);

        ownConsEstimatedLabel = getTv(R.id.summaryYieldEstimatedOwnConsValue);
        ownConsActualLabel = getTv(R.id.summaryYieldActualOwnConsValue);

        seedResEstimatedLabel = getTv(R.id.summaryYieldEstimatedSeedResValue);
        seedResActualLabel = getTv(R.id.summaryYieldActualSeedResValue);

        forMarketEstimatedLabel = getTv(R.id.summaryYieldEstimatedForMarketValue);
        forMarketActualLabel = getTv(R.id.summaryYieldActualForMarketValue);

        ownConsContainer = getView(R.id.ownConsContainer);
        seedResContainer = getView(R.id.seedResContainer);
        forMarketContainer = getView(R.id.forMarketContainer);

        return root;
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onMonitorButtonClicked();
        }
    };

    @Override
    public void attachPresenter(SummaryYieldContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTitle(@NonNull SummaryMenuItem.Label label) {
        final String title = getTitle(label);
        if (title == null) return;

        titleLabel.setText(title);
    }

    @Override
    public String getTitle(@NonNull SummaryMenuItem.Label label) {
        switch (label) {
            case PROBABLE_YIELD:
                return getString(R.string.summary_menu_estimated_probable_yield_label);
            case MAXIMUM_YIELD:
                return getString(R.string.summary_menu_max_yield_label);
            case MINIMUM_YIELD:
                return getString(R.string.summary_menu_min_yield_label);
        }
        return null;
    }

    @Override
    public void setUnit(@NonNull String unit) {
        unitEstimatedLabel.setText(unit);
        unitActualLabel.setText(unit);
    }

    @Override
    public void setUnitPrice(double estimated, double actual) {
        unitPriceEstimatedLabel.setText(String.valueOf(estimated));
        unitPriceActualLabel.setText(String.valueOf(actual));
    }

    @Override
    public void setTotalPrice(double estimated, double actual) {
        totalPriceEstimatedLabel.setText(String.valueOf(estimated));
        totalPriceActualLabel.setText(String.valueOf(actual));
    }

    @Override
    public void setOwnCons(double estimated, double actual) {
        ownConsEstimatedLabel.setText(String.valueOf(estimated));
        ownConsActualLabel.setText(String.valueOf(actual));
    }

    @Override
    public void setSeedRes(double estimated, double actual) {
        seedResEstimatedLabel.setText(String.valueOf(estimated));
        seedResActualLabel.setText(String.valueOf(actual));
    }

    @Override
    public void setForMarket(double estimated, double actual) {
        forMarketEstimatedLabel.setText(String.valueOf(estimated));
        forMarketActualLabel.setText(String.valueOf(actual));

    }

    @Override
    public void toggleOwnCons(boolean show) {
        ownConsContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleSeedRes(boolean show) {
        seedResContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void toggleForMarket(boolean show) {
        forMarketContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void openYieldFragment(@NonNull String clientId, @NonNull String cropACATId) {
        final Intent intent = new Intent(getActivity(), ACATEstimatedRevenueActivity.class);
        intent.putExtra(ACATEstimatedYieldActivity.ARG_MONITOR, true);
        intent.putExtra(ACATEstimatedYieldActivity.ARG_CLIENT_ID, clientId);
        intent.putExtra(ACATEstimatedYieldActivity.ARG_CROP_ACAT_ID, cropACATId);
        startActivity(intent);
        SummaryPresenter.selected = 2;
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
    public void close() {
        getParent().finish();
    }
}
