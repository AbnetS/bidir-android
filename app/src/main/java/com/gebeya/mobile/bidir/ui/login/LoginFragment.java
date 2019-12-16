package com.gebeya.mobile.bidir.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.R;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.base.remote.backend.sync.acat.ACATDataDownloadService;
import com.gebeya.mobile.bidir.impl.base.BaseFragment;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialog;
import com.gebeya.mobile.bidir.ui.common.dialogs.error.ErrorDialogCallback;
import com.gebeya.mobile.bidir.ui.common.dialogs.waiting.WaitingDialog;
import com.gebeya.mobile.bidir.ui.home.HomeActivity;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;

/**
 * View implementation for the LoginContract.View interface as a fragment
 */
public class LoginFragment extends BaseFragment implements LoginContract.View, ErrorDialogCallback {

    private static final int REQUEST_PLAY_SERVICES = 2000;

    private LoginContract.Presenter presenter;

    private ImageView logo;
    private EditText usernameInput;
    private EditText passwordInput;

    private WaitingDialog waitingDialog;
    private ErrorDialog errorDialog;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login, container, false);

        logo = getView(R.id.loginLogo);
        usernameInput = getEd(R.id.loginUsernameInput);
        passwordInput = getEd(R.id.loginPasswordInput);

//        getTv(R.id.loginForgotPasswordLabel).setOnClickListener(v -> presenter.onForgotPasswordPressed());

        getBt(R.id.loginButton).setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            presenter.onLoginPressed(username, password);
        });

        getView(R.id.loginAutoFillView).setOnClickListener(v -> {
            usernameInput.setText("new@test.com");
            passwordInput.setText("password");
        });

        return root;
    }

    @Override
    public void showUsernameMissingError() {
        usernameInput.setError(getString(R.string.login_error_username_prompt));
    }

    @Override
    public void showPasswordMissingError() {
        passwordInput.setError(getString(R.string.login_error_password_prompt));
    }

    @Override
    public void loadLogo(String url) {
        Picasso
                .with(getContext())
                .load(Constants.LOGO_URL)
                .error(R.drawable.splash_logo_full_export)
                .into(logo);
    }

    @Override
    public void showLoginProgress() {
        waitingDialog = WaitingDialog.newInstance(getString(R.string.login_progress_message));
        waitingDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void hideLoginProgress() {
        if (waitingDialog != null) {
            try {
                waitingDialog.dismiss();
                waitingDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showError(@NonNull Result result) {
        String message = getMessage(result.message);
        String extra = result.extra;
        if (message == null) {
            switch (result.message) {
                case ApiErrors.LOGIN_ERROR_INVALID_USERNAME_PASSWORD:
                    message = getString(R.string.login_error_invalid_username_password);
                    break;
                default:
                    message = getString(R.string.login_error_generic);
            }
        } else {
            extra = null;
        }

        errorDialog = ErrorDialog.newInstance(getString(R.string.login_error_title), message, extra);
        errorDialog.setCallback(this);
        errorDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void onErrorDialogDismissed() {

    }

    @Override
    public void hideError() {
        if (errorDialog != null) {
            try {
                errorDialog.dismiss();
                errorDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showSuccessMessage() {
        toast(R.string.login_success_message);
    }

    @Override
    public void openHomeScreen() {
        startActivity(new Intent(getContext(), HomeActivity.class));
    }

    @Override
    public void startDownloadService() {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.startService(new Intent(activity, ACATDataDownloadService.class));
        }
    }

    @Override
    public void openForgotPasswordScreen() {

    }

    @Override
    public void showPlayServicesErrorDialog(int status) {
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        final Dialog errorDialog = api.getErrorDialog(getActivity(), status, REQUEST_PLAY_SERVICES);
        errorDialog.show();
    }

    @Override
    public void attachPresenter(LoginContract.Presenter presenter) {
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
    public void onPause() {
        presenter.detachView();
        super.onPause();
    }

    @Override
    public void close() {
        getParent().finish();
    }
}