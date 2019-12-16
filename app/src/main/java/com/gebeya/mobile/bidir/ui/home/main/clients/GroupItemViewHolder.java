package com.gebeya.mobile.bidir.ui.home.main.clients;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.groups.remote.GroupParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

public class GroupItemViewHolder extends RecyclerView.ViewHolder implements ClientsContract.GroupItemView, View.OnClickListener{

    private ImageView imageView;
    private TextView groupNameLabel;
    private TextView leaderNameLabel;
    private TextView statusLabel;
    private TextView groupCountLabel;
    private TextView optionMenu;
    private View createdIndicatorView;

    private ClientsContract.Presenter presenter;

    public GroupItemViewHolder(View itemView, ClientsContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        imageView = itemView.findViewById(R.id.mainItemLayoutImageView);
        groupNameLabel = BaseActivity.getTv(R.id.mainItemLayoutGroupNameLabel, itemView);
        leaderNameLabel = BaseActivity.getTv(R.id.mainItemLayoutLeaderLabel, itemView);
        statusLabel = BaseActivity.getTv(R.id.mainItemLayoutStatusLabel, itemView);
        groupCountLabel = BaseActivity.getTv(R.id.textViewMemberCount, itemView);
        optionMenu = BaseActivity.getTv(R.id.textViewOptionsMenu, itemView);
        createdIndicatorView = itemView.findViewById(R.id.menuItemCreatedIndicatorView);

        optionMenu.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.textViewOptionsMenu) {
            presenter.onGroupOptionMenuClicked(optionMenu, getAdapterPosition());
        } else {
            presenter.onGroupSelected(getAdapterPosition());
        }
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
            case GroupParser.NEW:
                id = R.string.clients_fragment_status_new;
                break;
            case GroupParser.SCREENING_IN_PROGRESS:
                id = R.string.clients_fragment_status_screening_in_progress;
                break;
            case GroupParser.ELIGIBLE:
                id = R.string.clients_fragment_status_eligible;
                color = R.color.green;
                break;
            case GroupParser.INELIGIBLE:
                id = R.string.clients_fragment_status_ineligible;
                color = R.color.red;
                break;
            case GroupParser.LOAN_APPLICATION_NEW:
                id = R.string.clients_fragment_status_loan_application_new;
                break;
            case GroupParser.LOAN_APPLICATION_IN_PROGRESS:
                id = R.string.clients_fragment_status_loan_application_in_progress;
                break;
            case GroupParser.LOAN_APPLICATION_ACCEPTED:
                id = R.string.clients_fragment_status_loan_application_accepted;
                color = R.color.green;
                break;
            case GroupParser.LOAN_APPLICATION_DECLINED:
                id = R.string.clients_fragment_status_loan_application_declined;        // TODO: Look into the rejected final stuff on the UI level
                color = R.color.red;
                break;
            case GroupParser.ACAT_NEW:
                id = R.string. clients_fragment_status_acat_new;
                break;
            case GroupParser.ACAT_IN_PROGRESS:
                id = R.string.clients_fragment_status_acat_in_progress;
                break;
            case GroupParser.ACAT_SUBMITTED:
                id = R.string.clients_fragment_status_acat_submitted;
                break;
            case GroupParser.ACAT_RESUBMITTED:
                id = R.string.clients_fragment_status_acat_resubmitted;
                break;
            case GroupParser.ACAT_AUTHORIZED:
                id = R.string.clients_fragment_status_acat_authorized;
                color = R.color.green;
                break;
            case GroupParser.ACAT_DECLINED_FOR_REVIEW:
                id = R.string.clients_fragment_status_acat_declined_for_review;
                color = R.color.red;
                break;
            case GroupParser.LOAN_GRANTED:
                id = R.string.clients_fragment_status_loan_granted;
                color = R.color.green;
                break;
            case GroupParser.LOAN_PAID:
                id = R.string.clients_fragment_status_loan_paid;
                color = R.color.green;
                break;
            case GroupParser.LOAN_APPRAISAL_IN_PROGRESS:
                id = R.string.clients_fragment_status_loan_appraise_in_progress;
                break;
            case GroupParser.LOAN_PAYMENT_IN_PROGRESS:
                id = R.string.clients_fragment_status_loan_payment_in_progress;
                break;
            default:
                id = R.string.clients_fragment_status_unknown;
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
        createdIndicatorView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
