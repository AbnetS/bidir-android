package com.gebeya.mobile.bidir.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.ui.home.HomeActivity;
import com.gebeya.mobile.bidir.ui.login.LoginActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Implementation of the SplashContract.View view as a Fragment
 */
public class SplashFragment extends BaseFragment implements SplashContract.View {

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    private SplashContract.Presenter presenter;

    private ImageView logo;
    private Handler handler;

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            presenter.onLogoDisplayTimeout();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_splash, container, false);
        logo = getView(R.id.splashLogo);
        return root;
    }

    @Override
    public void openHomeScreen() {
        startActivity(new Intent(getContext(), HomeActivity.class));
    }

    @Override
    public void openLoginScreen() {
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    @Override
    public void startLogoDisplayTimeout() {
        handler.postDelayed(task, Constants.SPLASH_DELAY);
    }

    @Override
    public void stopLogoDisplayTimeout() {
        handler.removeCallbacks(task);
    }

    @Override
    public void loadLogo(String url) {
        Picasso
                .with(getContext())
                .load(url)
                .error(R.drawable.splash_logo_full_export)
                .into(logo, new Callback() {
                    @Override
                    public void onSuccess() {
                        presenter.onLogoLoaded();
                    }

                    @Override
                    public void onError() {
                        presenter.onLogoLoaded();
                    }
                });
    }

    @Override
    public void attachPresenter(SplashContract.Presenter presenter) {
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
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void close() {
        getActivity().finish();
    }
}