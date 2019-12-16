package com.gebeya.mobile.bidir.ui.home.main.screenings;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.complexscreening.remote.ComplexScreeningParser;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

/**
 * Screening View holder implementation for each item row representing a single screening
 * <p>
 * samkura47@gmail.com
 */
public class ComplexScreeningItemViewHolder extends RecyclerView.ViewHolder implements
        ScreeningsContract.ComplexScreeningItemView, View.OnClickListener{

    private ImageView imageView;
    private TextView nameLabel;
    private TextView statusLabel;
    private View createdIndicatorView;

    private ScreeningsContract.Presenter presenter;

    public ComplexScreeningItemViewHolder(View itemView, ScreeningsContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;

        imageView = itemView.findViewById(R.id.mainItemLayoutImageView);
        nameLabel = BaseActivity.getTv(R.id.mainItemLayoutNameLabel, itemView);
        statusLabel = BaseActivity.getTv(R.id.mainItemLayoutStatusLabel, itemView);
        createdIndicatorView = itemView.findViewById(R.id.mainItemLayoutCreatedIndicatorView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        presenter.onScreeningSelected(getAdapterPosition());
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
            case ComplexScreeningParser.STATUS_LOCAL_NEW:
                id = R.string.screenings_fragment_status_new;
                break;
            case ComplexScreeningParser.STATUS_LOCAL_IN_PROGRESS:
                id = R.string.screenings_fragment_status_in_progress;
                break;
            case ComplexScreeningParser.STATUS_LOCAL_SUBMITTED:
                id = R.string.screenings_fragment_status_submitted;
                break;
            case ComplexScreeningParser.STATUS_LOCAL_APPROVED:
                id = R.string.screenings_fragment_status_approved;
                color = R.color.green;
                break;
            case ComplexScreeningParser.STATUS_LOCAL_DECLINED_UNDER_REVIEW:
                id = R.string.screenings_fragment_status_declined_under_review;
                color = R.color.red;
                break;
            case ComplexScreeningParser.STATUS_LOCAL_DECLINED_FINAL:
                id = R.string.screenings_fragment_status_declined_final;
                color = R.color.red;
                break;
            default:
                id = R.string.screenings_fragment_status_unknown;
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
    public void setImage(@NonNull String picture) {
        Picasso
                .with(imageView.getContext())
                .load(picture)
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
