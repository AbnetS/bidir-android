package com.gebeya.mobile.bidir.ui.home.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.home.HomeActivity;
import com.gebeya.mobile.bidir.ui.login.LoginActivity;

/**
 * Implementation for the Main component View interface
 */
public class MainFragment extends BaseFragment implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    public static final int FRAGMENT_COUNT = 4;

    private MainContract.Presenter presenter;

    private String[] titles;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private DrawerLayout drawer;

    private MainPagerAdapter adapter;

    private View progressContainer;
    private TextView progressLabel;
    private ProgressBar progressBar;

    private ImageView navLogo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = getResources().getStringArray(R.array.main_fragment_titles);
        adapter = new MainPagerAdapter(getChildFragmentManager(), titles);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.main_fragment_layout, container, false);


        tabLayout = getView(R.id.loanProposalFragmentTabLayout);
        toolbar = getView(R.id.mainFragmentToolbar);

        toolbar.setNavigationIcon(R.drawable.main_menu_icon);
        getParent().setSupportActionBar(toolbar);


        viewPager = getView(R.id.mainFragmentViewPager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        progressContainer = getView(R.id.downloadProgressContainer);
        progressLabel = getTv(R.id.downloadProgressLabel);
        progressBar = getView(R.id.downloadProgressProgressBar);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        drawer = getParent().findViewById(R.id.drawer_layout);

        NavigationView navigationView = getParent().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);



        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void showLoggedOutMessage() {
        toast(R.string.login_logout_message);
    }

    @Override
    public void openLoginScreen() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    @Override
    public void showProgressContainer() {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> progressContainer.setVisibility(View.VISIBLE));
        }
    }

    @Override
    public void updateProgress(int progress) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                progressBar.setProgress(progress);
                final String text = getString(R.string.main_fragment_progress_message_data_label, progress);
                progressLabel.setText(text);
            });
        }
    }

    @Override
    public void hideProgressContainer() {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> progressContainer.setVisibility(View.INVISIBLE));
        }
    }

    @Override
    public void showDownloadCompleteMessage() {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(() -> toast(R.string.main_fragment_show_download_complete_message));
        }
    }

    @Override
    public void openSearch() {

    }

    @Override
    public void openNotificationsScreen() {

    }

    @Override
    public void openFilterMenu() {

    }

    @Override
    public void activateTab(int position) {

    }

    @Override
    public void attachPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void close() {
        getParent().finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                // TODO: Home functionality;
                break;
            case R.id.nav_about:
                // TODO: About info.
                break;
            case R.id.nav_logout:
                presenter.onNavLogOutPressedPressed();
                break;
        }

        return true;
    }
}
