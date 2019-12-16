package com.gebeya.mobile.bidir.ui.home.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gebeya.mobile.bidir.ui.home.main.acatapplications.ACATApplicationsFragment;
import com.gebeya.mobile.bidir.ui.home.main.clients.ClientsFragment;
import com.gebeya.mobile.bidir.ui.home.main.loanapplications.LoanApplicationsFragment;
import com.gebeya.mobile.bidir.ui.home.main.screenings.ScreeningsFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final String[] titles;

    public MainPagerAdapter(@NonNull FragmentManager manager, @NonNull String[] titles) {
        super(manager);
        this.titles = titles;
    }

    @Override
    @Nullable
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ClientsFragment();
            case 1: return new ScreeningsFragment();
            case 2: return new LoanApplicationsFragment();
            case 3: return new ACATApplicationsFragment();
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return MainFragment.FRAGMENT_COUNT;
    }
}