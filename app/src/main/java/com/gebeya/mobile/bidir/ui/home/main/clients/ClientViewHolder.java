package com.gebeya.mobile.bidir.ui.home.main.clients;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

/**
 * ViewHolder implementation for each item row representing a single client
 */
public class ClientViewHolder extends RecyclerView.ViewHolder implements ClientsContract.ClientItemView, View.OnClickListener {

    private ImageView imageView;
    private TextView nameLabel;
    private TextView statusLabel;
    private View createdIndicatorView;

    private ClientsContract.Presenter presenter;

    public ClientViewHolder(View itemView, ClientsContract.Presenter presenter) {
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
        presenter.onClientSelected(getAdapterPosition());
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
            case ClientParser.NEW:
                id = R.string.clients_fragment_status_new;
                break;
            case ClientParser.SCREENING_IN_PROGRESS:
                id = R.string.clients_fragment_status_screening_in_progress;
                break;
            case ClientParser.ELIGIBLE:
                id = R.string.clients_fragment_status_eligible;
                color = R.color.green;
                break;
            case ClientParser.INELIGIBLE:
                id = R.string.clients_fragment_status_ineligible;
                color = R.color.red;
                break;
            case ClientParser.LOAN_APPLICATION_NEW:
                id = R.string.clients_fragment_status_loan_application_new;
                break;
            case ClientParser.LOAN_APPLICATION_IN_PROGRESS:
                id = R.string.clients_fragment_status_loan_application_in_progress;
                break;
            case ClientParser.LOAN_APPLICATION_ACCEPTED:
                id = R.string.clients_fragment_status_loan_application_accepted;
                color = R.color.green;
                break;
            case ClientParser.LOAN_APPLICATION_REJECTED:
                id = R.string.clients_fragment_status_loan_application_declined;
                color = R.color.red;
                break;
            case ClientParser.LOAN_APPLICATION_DECLINED:
                id = R.string.clients_fragment_status_loan_application_declined;
                color = R.color.red;
                break;
            case ClientParser.ACAT_IN_PROGRESS:
                id = R.string.clients_fragment_status_acat_in_progress;
                break;
            case ClientParser.ACAT_SUBMITTED:
                id = R.string.clients_fragment_status_acat_submitted;
                break;
            case ClientParser.ACAT_RESUBMITTED:
                id = R.string.clients_fragment_status_acat_resubmitted;
                break;
            case ClientParser.ACAT_AUTHORIZED:
                id = R.string.clients_fragment_status_acat_authorized;
                color = R.color.green;
                break;
            case ClientParser.ACAT_DECLINED_FOR_REVIEW:
                id = R.string.clients_fragment_status_acat_declined_for_review;
                color = R.color.red;
                break;
            default:
                id = R.string.clients_fragment_status_unknown;
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
    public void setImage(@NonNull String photo) {
        Picasso
                .with(imageView.getContext())
                .load(photo)
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
                .into(imageView);
    }
}