package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

/**
 * Preliminary Question Item holder
 */

public class PreliminaryInformationItemHolder extends RecyclerView.ViewHolder implements
        PreliminaryInformationContract.PreliminaryQuestionItemView, View.OnClickListener {

    private CheckBox checkBox;
    private PreliminaryInformationContract.Presenter presenter;

    public PreliminaryInformationItemHolder(View itemView, PreliminaryInformationContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        checkBox = (CheckBox) itemView;
        checkBox.setOnClickListener(this);
    }

    @Override
    public void setChoice(@NonNull String choice, boolean checked) {
        checkBox.setText(choice);
        checkBox.setChecked(checked);
    }

    @Override
    public void onClick(View view) {
        presenter.onCropToggled(getAdapterPosition());
    }
}
