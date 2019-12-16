package com.gebeya.mobile.bidir.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseActivity;
import com.gebeya.mobile.bidir.impl.util.time.BaseReadableTime;
import com.gebeya.mobile.bidir.ui.home.main.MainContract;
import com.gebeya.mobile.bidir.ui.home.main.MainFragment;
import com.gebeya.mobile.bidir.ui.home.main.MainPresenter;
import com.gebeya.mobile.bidir.ui.home.tasks.TasksContract;
import com.gebeya.mobile.bidir.ui.home.tasks.TasksFragment;
import com.gebeya.mobile.bidir.ui.home.tasks.TasksPresenter;
import com.gebeya.mobile.bidir.ui.home.tasks.TasksState;

/**
 * Home Activity for holding the Main and Tasks fragments
 */
public class HomeActivity extends BaseActivity {

    private MainFragment mainFragment;
    private MainContract.Presenter mainPresenter;

    private TasksFragment tasksFragment;
    private TasksContract.Presenter tasksPresenter;

    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        toolbar = getView(R.id.home_activity_toolbar);
//        toolbar.setNavigationIcon(R.drawable.main_menu_icon);
//        setSupportActionBar(toolbar);
//
//        drawer = getView(R.id.drawer_layout);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
//                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        FragmentManager manager = getSupportFragmentManager();

        mainPresenter = new MainPresenter();

        mainFragment = (MainFragment) manager.findFragmentById(R.id.mainFragment);
        mainFragment.attachPresenter(mainPresenter);



        tasksFragment = (TasksFragment) getFragment(R.id.tasksFragmentContainer);
        if (tasksFragment == null) {
            tasksFragment = TasksFragment.newInstance();
            addFragment(tasksFragment, R.id.tasksFragmentContainer);
        }

        tasksPresenter = new TasksPresenter(
                TasksState.getInstance(),
                BaseReadableTime.getInstance()
        );

        tasksFragment.attachPresenter(tasksPresenter);
    }

    @Override
    public void onBackPressed() {
        if (drawer == null)
            return;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}