package com.gebeya.mobile.bidir.ui.questions.acat.initializeclientacat;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;

/**
 * Created by Samuel K. on 5/10/2018.
 * <p>
 * samkura47@gmail.com
 */

public class LoanProductSpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;
    private PreliminaryInformationContract.Presenter presenter;

    public LoanProductSpinnerAdapter(@NonNull Context context, PreliminaryInformationContract.Presenter presenter) {
        super(context, R.layout.spinner_item_layout);
        inflater = LayoutInflater.from(context);
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        Holder holder;
        if (root == null) {
            root = inflater.inflate(R.layout.spinner_item_layout, parent, false);
            holder = new Holder();

            holder.spinnerItem = root.findViewById(R.id.spinnerItemLabel);
            root.setTag(holder);
        } else {
            holder = (Holder) root.getTag();
        }

        presenter.onBindProductRowItem(holder, position);

        return root;
    }

    @Override
    public int getCount() {
        return presenter.getLoanProductCount();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    private static class Holder implements PreliminaryInformationContract.LoanProductItemView  {
        TextView spinnerItem;

        @Override
        public void setName(@NonNull String name) {
            spinnerItem.setText(name);
        }
    }
}
