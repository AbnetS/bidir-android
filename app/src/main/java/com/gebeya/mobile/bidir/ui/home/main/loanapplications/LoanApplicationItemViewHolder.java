package com.gebeya.mobile.bidir.ui.home.main.loanapplications;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.complexloanapplication.remote.ComplexLoanApplicationParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

/**
 * Item ViewHolder for a single {@link com.gebeya.mobile.bidir.data.loanapplication.LoanApplication}
 */
public class LoanApplicationItemViewHolder extends RecyclerView.ViewHolder implements
        LoanApplicationsContract.LoanApplicationItemView, View.OnClickListener {

    private ImageView imageView;
    private TextView nameLabel;
    private TextView statusLabel;
    private View createdIndicatorView;

    private LoanApplicationsContract.Presenter presenter;

    public LoanApplicationItemViewHolder(View itemView, LoanApplicationsContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;

        imageView = itemView.findViewById(R.id.mainItemLayoutImageView);
        nameLabel = BaseActivity.getTv(R.id.mainItemLayoutNameLabel, itemView);
        statusLabel = BaseActivity.getTv(R.id.mainItemLayoutStatusLabel, itemView);
        createdIndicatorView = itemView.findViewById(R.id.mainItemLayoutCreatedIndicatorView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        presenter.onLoanApplicationSelected(getAdapterPosition());
    }

    @Override
    public void setName(@NonNull String name) {
        nameLabel.setText(name);
    }

    @Override
    public void setStatus(@NonNull String status) {
        int id;
        int color = R.color.gray;
        switch (status) {
            case ComplexLoanApplicationParser.STATUS_LOCAL_NEW:
                id = R.string.loan_applications_fragment_status_new;
                break;
            case ComplexLoanApplicationParser.STATUS_LOCAL_IN_PROGRESS:
                id = R.string.loan_applications_fragment_status_in_progress;
                break;
            case ComplexLoanApplicationParser.STATUS_LOCAL_SUBMITTED:
                id = R.string.loan_applications_fragment_status_submitted;
                break;
            case ComplexLoanApplicationParser.STATUS_LOCAL_ACCEPTED:
                id = R.string.loan_applications_fragment_status_accepted;
                color = R.color.green;
                break;
            case ComplexLoanApplicationParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW:
                id = R.string.loan_applications_fragment_status_declined_under_review;
                color = R.color.red;
                break;
            case ComplexLoanApplicationParser.STATUS_LOCAL_DECLINED_FINAL:
                id = R.string.loan_applications_fragment_status_declined_final;
                color = R.color.red;
                break;
            default:
                id = R.string.loan_applications_fragment_status_unknown;
                color = R.color.red;
        }

        statusLabel.setText(id);
        statusLabel.setTextColor(ContextCompat.getColor(statusLabel.getContext(), color));
    }

    @Override
    public void toggleCreatedIndicator(boolean show) {
        createdIndicatorView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setImage(@NonNull String pictureUrl) {
        Picasso
                .with(imageView.getContext())
                .load(pictureUrl)
                .fit()
                .centerCrop()
                .transform(new RoundedTransformationBuilder()
                        .borderWidthDp(1)
                        .borderColor(
                                ContextCompat.getColor(
                                        imageView.getContext(),
                                        R.color.gray_dark
                                ))
                        .cornerRadiusDp(40)
                        .oval(false)
                        .build())
                .error(R.drawable.register_personal_details_picture_sample)
                .into(imageView);
    }
}
