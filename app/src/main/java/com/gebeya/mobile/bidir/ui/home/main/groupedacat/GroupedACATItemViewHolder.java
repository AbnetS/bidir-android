package com.gebeya.mobile.bidir.ui.home.main.groupedacat;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.acatapplication.remote.ACATApplicationParser;
import com.gebeya.mobile.bidir.data.client.remote.ClientParser;
import com.gebeya.mobile.bidir.data.groupedacat.remote.GroupedACATParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

public class GroupedACATItemViewHolder extends RecyclerView.ViewHolder implements
        GroupedACATContract.GroupedACATsItemView, View.OnClickListener {

    private GroupedACATContract.Presenter presenter;

    private ImageView imageView;
    private TextView nameLabel;
    private TextView statusLabel;
    private TextView optionMenu;

    public GroupedACATItemViewHolder(View itemView, GroupedACATContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        imageView = itemView.findViewById(R.id.mainItemLayoutImageView);
        nameLabel = BaseActivity.getTv(R.id.mainItemLayoutNameLabel, itemView);
        statusLabel = BaseActivity.getTv(R.id.mainItemLayoutStatusLabel, itemView);
        optionMenu = BaseActivity.getTv(R.id.textViewOptionsMenu, itemView);

        itemView.setOnClickListener(this);
        optionMenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.textViewOptionsMenu) {
            presenter.onOptionMenuClicked(optionMenu, getAdapterPosition());
        } else {
            presenter.onACATSelected(getAdapterPosition());
        }
    }

    @Override
    public void setName(@NonNull String name) {
        nameLabel.setText(name);
    }

    @Override
    public void setStatus(@NonNull String status) {
        int color = R.color.gray;
        int id;
        switch (status) {
            case ACATApplicationParser.STATUS_LOCAL_IN_PROGRESS:
                id = R.string.grouped_acat_applications_fragment_status_in_progress;
                break;
            case ACATApplicationParser.STATUS_LOCAL_SUBMITTED:
                id = R.string.grouped_acat_applications_fragment_status_submitted;
                break;
            case ACATApplicationParser.STATUS_LOCAL_RESUBMITTED:
                id = R.string.grouped_acat_applications_fragment_status_resubmitted;
                break;
            case ACATApplicationParser.STATUS_LOCAL_AUTHORIZED:
                id = R.string.grouped_acat_applications_fragment_status_accepted;
                color = R.color.green;
                break;
            case ACATApplicationParser.STATUS_LOCAL_DECLINED_FOR_REVIEW:
                id = R.string.grouped_acat_applications_fragment_status_declined_under_review;
                color = R.color.red;
                break;
            case ClientParser.LOAN_APPLICATION_ACCEPTED:
                color = R.color.gray;
                id = R.string.grouped_acat_applications_fragment_status_new;
                break;
            default:
                color = R.color.gray;
                id = R.string.grouped_acat_applications_fragment_status_unknown;
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
    public void toggleCreatedIndicator(boolean show) {

    }
}
