package com.gebeya.mobile.bidir.ui.home.main.screenings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.screening.ComplexScreeningSyncService;
import com.gebeya.mobile.bidir.data.groups.Group;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.ui.form.screening.ScreeningActivity;
import com.gebeya.mobile.bidir.ui.home.main.groupedscreenings.GroupedScreeningsActivity;

/**
 * Implementation of the Screening view contract interface
 */
public class ScreeningsFragment extends BaseFragment implements ScreeningsContract.View {

    private ScreeningsContract.Presenter presenter;

    private RecyclerView recyclerView;
    private ScreeningsRecyclerViewAdapter individualAdapter;
    private GroupedScreeningRecyclerViewAdapter groupedAdapter;
    private TextView noClientsLabel;
    private TextView noGroupClientsLabel;
    private TextView individualLabel;
    private TextView groupLabel;

    private TextView screeningsFilter;

    private PopupMenu individualPopupMenu, groupPopupMenu;
    private Context wrapper;

    private View syncRoot;
    private View syncIcon;
    private View syncProgress;
    private View syncStatus;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter(new ScreeningsPresenter());

        // Allows loading of Vector images on pre API 21 devices
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_screenings, container, false);
        recyclerView = getView(R.id.screeningsRecyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(getParent());
        recyclerView.setLayoutManager(manager);

        individualAdapter = new ScreeningsRecyclerViewAdapter(presenter);
        groupedAdapter = new GroupedScreeningRecyclerViewAdapter(presenter);

        noClientsLabel = getTv(R.id.screeningsNoScreeningsLabel);
        noGroupClientsLabel = getTv(R.id.screeningsNoGroupedScreeningsLabel);

        screeningsFilter = getTv(R.id.screeningsFragmentFilterLabel);
        screeningsFilter.setOnClickListener(v -> presenter.onScreeningFilterClicked());

        wrapper = new ContextThemeWrapper(getContext(), R.style.popupMenuStyle);
        individualPopupMenu = new PopupMenu(wrapper, screeningsFilter);
        individualPopupMenu.inflate(R.menu.screenings_filter_menu);

        groupPopupMenu = new PopupMenu(wrapper, screeningsFilter);
        groupPopupMenu.inflate(R.menu.group_screenings_filter_menu);

        individualLabel = getTv(R.id.loanTypeIndividualLabel);
        groupLabel = getTv(R.id.loanTypeGroupLabel);

//        individualLabel.setVisibility(View.GONE);
//        groupLabel.setVisibility(View.GONE);

        individualLabel.setOnClickListener(view -> {
            presenter.onIndividualButtonPressed();
            individualPopupMenu.getMenu().getItem(0).setChecked(true);
        });
        groupLabel.setOnClickListener(view -> {
            presenter.onGroupButtonPressed();
            groupPopupMenu.getMenu().getItem(0).setChecked(true);
        });


        syncRoot = getView(R.id.syncIndicatorRoot);
        syncIcon = getView(R.id.syncIndicatorIcon);
        syncProgress = getView(R.id.syncIndicatorProgressWheel);
        syncStatus = getView(R.id.syncIndicatorStatusView);
        syncRoot.setOnClickListener(v -> presenter.onSyncPressed());

        return root;
    }

    @Override
    public void showSyncInProgress() {
        syncIcon.setVisibility(View.GONE);
        syncProgress.setVisibility(View.VISIBLE);
        syncStatus.setVisibility(View.GONE);
    }

    @Override
    public void showSyncIdle(boolean hasUnSyncedData) {
        syncIcon.setVisibility(View.VISIBLE);
        syncProgress.setVisibility(View.GONE);
        syncStatus.setVisibility(hasUnSyncedData ? View.VISIBLE : View.GONE);
    }

    @Override
    public void startSyncService() {
        final Context context = getContext();
        if (context == null) return;

        final Intent intent = new Intent(context, ComplexScreeningSyncService.class);
        context.startService(intent);
    }

    @Override
    public void openScreening(@NonNull String screeningId) {
        final Intent intent = new Intent(getContext(), ScreeningActivity.class);
        intent.putExtra(ScreeningActivity.ARG_SCREENING_ID, screeningId);
        startActivity(intent);
    }

    @Override
    public void toggleIndividualButton(boolean isEnabled) {
        if (isEnabled) {
            individualLabel.setTextColor(getResources().getColor(R.color.green));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_selected, 0, 0);
        } else {
            individualLabel.setTextColor(getResources().getColor(R.color.gray_light));
            individualLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_individual_default, 0, 0);
        }
    }

    @Override
    public void toggleGroupButton(boolean isEnabled) {
        if (isEnabled) {
            groupLabel.setTextColor(getResources().getColor(R.color.green));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_selected, 0, 0);
        } else {
            groupLabel.setTextColor(getResources().getColor(R.color.gray_light));
            groupLabel.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_group_default, 0, 0);
        }
    }

    @Override
    public void openGroupedScreeningScreen(@NonNull Group group, @NonNull String title) {
        final Intent intent = new Intent(getContext(), GroupedScreeningsActivity.class);
        intent.putExtra(GroupedScreeningsActivity.ARG_GROUP_ID, group._id);
        intent.putExtra(GroupedScreeningsActivity.ARG_TITLE, title);
        startActivity(intent);
    }

    @Override
    public void openIndividualFilterMenu() {
        individualPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadScreenings();
                    return true;
                case R.id.newScreeningsMenu:
                    menuItem.setChecked(true);
                    presenter.loadNewScreenings();
                    return true;
                case R.id.inProgressScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadInprogressScreenings();
                    return true;
                case R.id.submittedScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadSubmittedScreenings();
                    return true;
                case R.id.eligibleScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadEligibleScreenings();
                    return true;
                case R.id.rejectedScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadRejectedScreenings();
                    return true;
            }
            return false;
        });
        individualPopupMenu.show();
    }

    @Override
    public void openGroupFilterMenu() {
        groupPopupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.allGroupScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupApplication();
                    return true;
                case R.id.newGroupScreeningsMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedNewScreenings();
                    return true;
                case R.id.inProgressGroupScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedInprogressScreenings();
                    return true;
                case R.id.submittedGroupScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedSubmittedScreenings();
                    return true;
                case R.id.eligibleGroupScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedEligibleScreenings();
                    return true;
                case R.id.rejectedGroupScreeningMenu:
                    menuItem.setChecked(true);
                    presenter.loadGroupedRejectedScreenings();
                    return true;
            }

            return false;
        });
        groupPopupMenu.show();
    }

    @Override
    public void showGrouped() {
        recyclerView.setAdapter(groupedAdapter);
    }

    @Override
    public void showScreenings() {
        recyclerView.setAdapter(individualAdapter);
    }

    @Override
    public void toggleNoScreeningsLabel(boolean show) {
        noClientsLabel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void toggleNoGroupScreeningsLabel(boolean show) {
        noGroupClientsLabel.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void attachPresenter(ScreeningsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        individualPopupMenu.getMenu().getItem(0).setChecked(true);
        groupPopupMenu.getMenu().getItem(0).setChecked(true);
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
}