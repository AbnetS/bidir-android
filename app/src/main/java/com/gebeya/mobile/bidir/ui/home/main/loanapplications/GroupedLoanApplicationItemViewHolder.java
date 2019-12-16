package com.gebeya.mobile.bidir.ui.home.main.loanapplications;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.remote.GroupedComplexLoanParser;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

public class GroupedLoanApplicationItemViewHolder extends RecyclerView.ViewHolder implements
        LoanApplicationsContract.GroupItemView, View.OnClickListener {

    private ImageView imageView;
    private TextView groupNameLabel;
    private TextView leaderNameLabel;
    private TextView statusLabel;
    private TextView groupCountLabel;

    private LoanApplicationsContract.Presenter presenter;

    public GroupedLoanApplicationItemViewHolder(View itemView, LoanApplicationsContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        imageView = itemView.findViewById(R.id.mainItemLayoutImageView);
        groupNameLabel = BaseActivity.getTv(R.id.mainItemLayoutGroupNameLabel, itemView);
        leaderNameLabel = BaseActivity.getTv(R.id.mainItemLayoutLeaderLabel, itemView);
        statusLabel = BaseActivity.getTv(R.id.mainItemLayoutStatusLabel, itemView);
        groupCountLabel = BaseActivity.getTv(R.id.textViewMemberCount, itemView);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        presenter.onGroupSelected(getAdapterPosition());
    }

    @Override
    public void setGroupName(@NonNull String groupName) {
        groupNameLabel.setText(groupName);
    }

    @Override
    public void setLeaderName(@Nullable String leaderName) {
        if (leaderName != null) {
            leaderNameLabel.setText(leaderName);
        } else {
            leaderNameLabel.setText(R.string.group_leader_missing_prompt);
            leaderNameLabel.setTextColor(ContextCompat.getColor(statusLabel.getContext(), R.color.red));
        }
    }

    @Override
    public void setStatus(@NonNull String status) {
        int id;
        int color = R.color.gray;
        switch (status) {
            case GroupedComplexLoanParser.STATUS_LOCAL_NEW:
                id = R.string.group_loan_fragment_status_new;
                break;
            case GroupedComplexLoanParser.STATUS_LOCAL_IN_PROGRESS:
                id = R.string.group_loan_fragment_status_in_progress;
                break;
            case GroupedComplexLoanParser.STATUS_LOCAL_SUBMITTED:
                id = R.string.group_loan_fragment_status_submitted;
                color = R.color.green;
                break;
            case GroupedComplexLoanParser.STATUS_LOCAL_ACCEPTED:
                id = R.string.group_loan_fragment_status_accepted;        // TODO: Look into the rejected final stuff on the UI level
                color = R.color.green;
                break;
            case GroupedComplexLoanParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW:
                id = R.string.group_loan_fragment_status_declined_under_review;
                color = R.color.red;
                break;
            case GroupedComplexLoanParser.STATUS_LOCAL_DECLINED_FINAL:
                id = R.string.group_loan_fragment_status_declined_final;
                color = R.color.red;
                break;
            default:
                id = R.string.group_loan_fragment_status_unknown;
                color = R.color.red;
        }

        statusLabel.setText(id);
        statusLabel.setTextColor(ContextCompat.getColor(statusLabel.getContext(), color));
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

    @Override
    public void setGroupCount(@NonNull int groupCount) {
        groupCountLabel.setText(String.valueOf(groupCount));
    }

    @Override
    public void toggleCreatedIndicator(boolean show) {

    }
}
