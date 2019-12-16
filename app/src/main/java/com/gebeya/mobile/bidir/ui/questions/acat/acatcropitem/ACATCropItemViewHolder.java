package com.gebeya.mobile.bidir.ui.questions.acat.acatcropitem;

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

/**
 * Created by Samuel K. on 7/11/2018.
 * <p>
 * samkura47@gmail.com
 */

public class ACATCropItemViewHolder extends RecyclerView.ViewHolder implements ACATCropItemContract.ACATCropItemView, View.OnClickListener {
    ImageView imageView;
    TextView cropLabel;
    TextView statusLabel;
    TextView optionsMenu;

    private ACATCropItemContract.Presenter presenter;

    public ACATCropItemViewHolder(View itemView, ACATCropItemContract.Presenter presenter) {
        super(itemView);
        this.presenter = presenter;

        imageView = itemView.findViewById(R.id.mainItemLayoutImageView);
        cropLabel = BaseActivity.getTv(R.id.mainItemLayoutNameLabel, itemView);
        statusLabel = BaseActivity.getTv(R.id.mainItemLayoutStatusLabel, itemView);
        optionsMenu = BaseActivity.getTv(R.id.textViewOptionsMenu, itemView);

        itemView.setOnClickListener(this);
        optionsMenu.setOnClickListener(this);
        optionsMenu.setVisibility(presenter.optionMenuVisibility());
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.textViewOptionsMenu) {
            presenter.onOptionMenuClicked(optionsMenu, getAdapterPosition());
        } else {
            presenter.onACATCropItemSelected(getAdapterPosition());
        }
    }


    @Override
    public void setName(@NonNull String name) {
        cropLabel.setText(name);
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
