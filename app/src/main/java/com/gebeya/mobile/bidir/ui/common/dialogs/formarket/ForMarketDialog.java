package com.gebeya.mobile.bidir.ui.common.dialogs.formarket;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.EditText;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseDialog;

/**
 * Created by Samuel K. on 8/30/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ForMarketDialog extends BaseDialog {

    private static final String ARG_UNION_AMOUNT = "UNION";
    private static final String ARG_FACTORY_AMOUNT = "FACTORY";
    private static final String ARG_TRADER_AMOUNT = "TRADER";
    private static final String ARG_UNION_REMARK = "UNION_REMARK";
    private static final String ARG_FACTORY_REMARK = "FACTORY_REMARK";
    private static final String ARG_TRADER_REMARK = "TRADER_REMARK";

    private static final String ARG_IS_EXPECTED = "IS_EXPECTED";

    public static ForMarketDialog newInstance(@NonNull String union,
                                              @NonNull String factory,
                                              @NonNull String trader,
                                              @Nullable String unionRemark,
                                              @Nullable String factoryRemark,
                                              @Nullable String traderRemark,
                                              boolean isExpected) {
        ForMarketDialog dialog = new ForMarketDialog();
        Bundle arg = new Bundle();

        arg.putString(ARG_UNION_AMOUNT, union);
        arg.putString(ARG_FACTORY_AMOUNT, factory);
        arg.putString(ARG_TRADER_AMOUNT, trader);
        arg.putString(ARG_UNION_REMARK, unionRemark);
        arg.putString(ARG_FACTORY_REMARK, factoryRemark);
        arg.putString(ARG_TRADER_REMARK, traderRemark);
        arg.putBoolean(ARG_IS_EXPECTED, isExpected);

        dialog.setArguments(arg);
        return dialog;
    }

    private EditText unionEstimated;
    private EditText factoryEstimated;
    private EditText traderEstimated;

    private EditText unionActual;
    private EditText factoryActual;
    private EditText traderActual;

    private EditText unionRemark;
    private EditText factoryRemark;
    private EditText traderRemark;

    private boolean isExpected;


    private ForMarketCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateRoot(R.layout.dialog_for_market_detail_layout);

        Bundle arg = getArguments();

        final String union = arg.getString(ARG_UNION_AMOUNT);
        final String factory = arg.getString(ARG_FACTORY_AMOUNT);
        final String trader = arg.getString(ARG_TRADER_AMOUNT);

        final boolean isExpected = arg.getBoolean(ARG_IS_EXPECTED, true);
        this.isExpected = isExpected;

        final String unionRrk = arg.getString(ARG_UNION_REMARK);
        final String factoryRrk = arg.getString(ARG_FACTORY_REMARK);
        final String traderRrk = arg.getString(ARG_TRADER_REMARK);

        unionEstimated = getEd(R.id.forMarketUnionExpectedAmountInput);
        unionActual = getEd(R.id.forMarketUnionActualAmountInput);

        factoryEstimated = getEd(R.id.forMarketFactoryExpectedAmountInput);
        factoryActual = getEd(R.id.forMarketFactoryActualAmountInput);

        traderEstimated = getEd(R.id.forMarketTraderExpectedAmountInput);
        traderActual = getEd(R.id.forMarketTraderActualAmountInput);

        unionRemark = getEd(R.id.forMarketUnionRemarkInput);
        factoryRemark = getEd(R.id.forMarketFactoryRemarkInput);
        traderRemark = getEd(R.id.forMarketTraderRemarkInput);

        getBt(R.id.forMarketDialogOKButton).setOnClickListener(view -> {

            callback.forMarketUnionCallback(getUnionInput(), getUnionRemark(), this.isExpected);
            callback.forMarketFactoryCallback(getFactoryInput(), getFactoryRemark(), this.isExpected);
            callback.forMarketUnionCallback(getTraderInput(), getTraderRemark(), this.isExpected);

            dismiss();
        });

        getBt(R.id.forMarketDialogCancelButton).setOnClickListener(view -> dismiss());

        return createDialog();
    }

    public void setCallback(@NonNull ForMarketCallback callback) {
        this.callback = callback;
    }

    private String getUnionInput() {
        final String union = isExpected ?
                unionEstimated.getText().toString().trim() :
                unionActual.getText().toString().trim();

        return union;
    }

    private String getFactoryInput() {
        final String factory = isExpected ?
                factoryEstimated.getText().toString().trim() :
                factoryActual.getText().toString().trim();

        return factory;
    }

    private String getTraderInput() {
        final String trader = isExpected ?
                traderEstimated.getText().toString().trim() :
                traderActual.getText().toString().trim();

        return trader;
    }

    private String getUnionRemark() {
        final String unionRmrk = unionRemark.getText().toString().trim();

        return unionRmrk;
    }

    private String getFactoryRemark() {
        final String factoryRmrk = factoryRemark.getText().toString().trim();

        return factoryRmrk;
    }

    private String getTraderRemark() {
        final String traderRmrk = traderRemark.getText().toString().trim();

        return traderRmrk;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);
    }
}
