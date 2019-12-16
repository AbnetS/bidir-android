package com.gebeya.mobile.bidir.ui.home.main.groupedscreenings;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

public class GroupedScreeningsItemViewHolder extends RecyclerView.ViewHolder implements
        GroupedScreeningsContract.GroupedScreeningsItemView, View.OnClickListener {

    private ImageView imageView;
    private TextView nameLabel;
    private TextView statusLabel;

    private GroupedScreeningsContract.Presenter presenter;

    public GroupedScreeningsItemViewHolder(View itemView, GroupedScreeningsContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;
        imageView = itemView.findViewById(R.id.mainItemLayoutImageView);
        nameLabel = BaseActivity.getTv(R.id.mainItemLayoutNameLabel, itemView);
        statusLabel = BaseActivity.getTv(R.id.mainItemLayoutStatusLabel, itemView);

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
        statusLabel.setText(status);
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
