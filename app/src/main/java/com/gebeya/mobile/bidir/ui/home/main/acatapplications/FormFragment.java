package com.gebeya.mobile.bidir.ui.home.main.acatapplications;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;

/**
 * Implementation of the form View interface contract
 */
public class FormFragment extends BaseFragment {

    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_form, container, false);

        return root;
    }
}