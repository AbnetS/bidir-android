package com.gebeya.mobile.bidir.ui.questions.acat.acatcostestimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.ui.questions.acat.acatitemcostestimation.ACATItemCostEstimationFragment;
import com.gebeya.mobile.bidir.ui.summary.SummaryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel K. on 5/20/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCostEstimationActivity extends BaseActivity {

    public static final String ARG_CLIENT_ID = "client_id";
    public static final String IS_EDITING = "isEditing";
    public static final String ARG_CROP_ACAT_ID = "crop_acat_id";
    public static final String IS_MONITORING = "isMonitoring";
    public static final String SECTION_ID = "section_id";
    public static final String SUBTOTAL_LABEL = "subtotalLabel";
    public static final String TITLE_LABEL = "title_label";

    private boolean isMonitoring;
    private String clientId;
    private String cropACATID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acat_questions);

        final Intent intent = getIntent();

        final String clientId = intent.getStringExtra(ARG_CLIENT_ID);
        this.clientId = clientId;
        if (clientId == null) throw new NullPointerException("Client Id is null.");

        final boolean isEditing = intent.getBooleanExtra(IS_EDITING, false);
        final boolean isMonitoring = intent.getBooleanExtra(IS_MONITORING, false);
        this.isMonitoring = isMonitoring;
        final String cropACATId = intent.getStringExtra(ARG_CROP_ACAT_ID);
        this.cropACATID = cropACATId;
        final String sectionId = intent.getStringExtra(SECTION_ID);
        final String subTotalLabel = intent.getStringExtra(SUBTOTAL_LABEL);
        final ArrayList<String> titleLabel = intent.getStringArrayListExtra(TITLE_LABEL);




        if (isMonitoring) {
            final Bundle args = new Bundle();
            args.putString(ACATItemCostEstimationFragment.CLIENT_ID, clientId);
            args.putString(ACATItemCostEstimationFragment.CROP_ACAT_ID, cropACATId);
            args.putBoolean(IS_EDITING, isEditing);
            args.putBoolean(ACATCostEstimationActivity.IS_MONITORING, isMonitoring);
            args.putString(ACATItemCostEstimationFragment.SECTION_ID, sectionId);
            args.putString(ACATItemCostEstimationFragment.SUBTOTAL_LABEL, subTotalLabel);
            args.putStringArrayList(ACATItemCostEstimationFragment.TITLE_LABEL, titleLabel);

            ACATItemCostEstimationFragment fragment = (ACATItemCostEstimationFragment) getFragment(R.id.questionsACATContainer);

            if (fragment == null) {
                fragment = ACATItemCostEstimationFragment.newInstance();
                fragment.setArguments(args);
                addFragment(fragment, R.id.questionsACATContainer);
            }

        } else {
            final Bundle args = new Bundle();
            args.putString(ARG_CLIENT_ID, clientId);
            args.putString(ARG_CROP_ACAT_ID, cropACATId);
            args.putBoolean(IS_EDITING, isEditing);
            args.putBoolean(IS_MONITORING, isMonitoring);

            ACATCostEstimationFragment fragment = (ACATCostEstimationFragment) getFragment(R.id.questionsACATContainer);

            if (fragment == null) {
                fragment = ACATCostEstimationFragment.newInstance();
                fragment.setArguments(args);
                replaceFragment(fragment, R.id.questionsACATContainer);
            }
        }
//        fragment = (ACATCostEstimationFragment) getFragment(R.id.questionsACATContainer);
//
//        if (fragment == null) {
//            fragment = ACATCostEstimationFragment.newInstance();
//            fragment.setArguments(args);
//            addFragment(fragment, R.id.questionsACATContainer);
//        }
    }

    @Override
    public void onBackPressed() {
        if (isMonitoring) {
            final Intent intent = new Intent(this, SummaryActivity.class);
            intent.putExtra(SummaryActivity.ARG_CLIENT_ID, clientId);
            intent.putExtra(SummaryActivity.ARG_CROP_ACAT_ID, cropACATID);
            startActivity(intent);
        }
        finish();
    }
}
