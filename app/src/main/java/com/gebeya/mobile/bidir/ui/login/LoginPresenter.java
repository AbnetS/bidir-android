package com.gebeya.mobile.bidir.ui.login;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.acatapplication.repo.ACATApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.acatform.ACATFormRepoSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.complexloanapplication.repo.ComplexLoanApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.form.repo.ComplexFormRepositorySource;
import com.gebeya.mobile.bidir.data.groupedacat.repo.GroupedACATRepoSource;
import com.gebeya.mobile.bidir.data.groupedcomplexloan.repo.GroupedComplexLoanRepoSource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo.GroupedComplexScreeningRepoSource;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.loanProposal.repo.LoanProposalRepoSource;
import com.gebeya.mobile.bidir.data.loanproduct.LoanProductRepoSource;
import com.gebeya.mobile.bidir.data.task.TaskRepoSource;
import com.gebeya.mobile.bidir.data.user.UserRepoSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.impl.util.location.play_services.PlayServicesApi;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;

import javax.inject.Inject;

/**
 * Login presenter implementation
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    @Inject UserRepoSource repo;
    @Inject ClientRepoSource clientRepo;
    @Inject ComplexScreeningRepositorySource complexScreeningRepo;
    @Inject ComplexLoanApplicationRepositorySource complexLoanApplicationRepo;
    @Inject ACATApplicationRepositorySource acatApplicationRepositorySource;
    @Inject ComplexFormRepositorySource complexFormRepo;
    @Inject LoanProductRepoSource loanProductRepo;
    @Inject LoanProposalRepoSource loanProposalRepo;
    @Inject ACATFormRepoSource acatFormRepo;
    @Inject PlayServicesApi playServicesApi;
    @Inject GroupRepoSource groupRepo;
    @Inject GroupedComplexScreeningRepoSource groupScreeningRepo;
    @Inject GroupedComplexLoanRepoSource groupedLoanRepo;
    @Inject GroupedACATRepoSource groupedACATRepo;
    @Inject
    TaskRepoSource  taskRepoSource;
    @Inject SchedulersProvider schedulers;

    private RequestState state;

    public LoginPresenter(@NonNull RequestState state) {
        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
        this.state = state;
    }

    @Override
    public void start() {
        view.loadLogo(Constants.LOGO_URL);

        checkPlayServices();

        if (state.loading()) {
            view.showLoginProgress();
            return;
        }

        if (state.complete()) {
            onLoginComplete();
            return;
        }

        if (state.error()) {
            onLoginFailed(state.getError());
        }
    }

    private void checkPlayServices() {
        if (playServicesApi.missingPlayServices()) {
            if (playServicesApi.isUserResolvableError()) {
                final int status = playServicesApi.getStatus();
                view.showPlayServicesErrorDialog(status);
            }
        }
    }

    @Override
    @SuppressLint("CheckResult")
    public void onLoginPressed(@NonNull String username, @NonNull String password) {
        if (state.loading()) return;

        if (username.trim().isEmpty()) {
            view.showUsernameMissingError();
            return;
        }

        if (password.trim().isEmpty()) {
            view.showPasswordMissingError();
            return;
        }

        state.setLoading();
        view.showLoginProgress();

        repo.login(username, password)
                .flatMap(user -> clientRepo.fetchForceAll())
                .flatMap(done -> groupRepo.fetchForceAll())
                .flatMap(data -> complexScreeningRepo.fetchForceAll())
                .flatMap(done -> complexLoanApplicationRepo.fetchForceAll())
//                .flatMap(done -> acatApplicationRepositorySource.fetchForceAll())
                .flatMap(done -> complexFormRepo.fetchForceAll())
                .flatMap(done -> groupScreeningRepo.fetchForceAll())
                .flatMap(done -> groupedLoanRepo.fetchForceAll())
                .flatMap(done -> groupedACATRepo.fetchForceAll())
                .flatMap(done -> loanProductRepo.fetchForceAll())
                .flatMap(done -> loanProposalRepo.fetchForceAll())
                .flatMap(done -> taskRepoSource.fetchForceAll())

                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                            state.setComplete();
                            onLoginComplete();
                        },
                        throwable -> {
                            state.setError(throwable);

                            throwable.printStackTrace();
                            repo.logout();
                            onLoginFailed(throwable);
                        });
    }

    @Override
    public void onLoginComplete() {
        if (view == null) return;

        view.showSuccessMessage();
        view.openHomeScreen();
        view.startDownloadService();
        view.close();

        state.reset();
    }

    @Override
    public void onLoginFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = null;
        String extra = null;

        if (error.contains(ApiErrors.LOGIN_PART_INVALID_USERNAME_PASSWORD)) {
            message = ApiErrors.LOGIN_ERROR_INVALID_USERNAME_PASSWORD;
        }
        if (error.contains(ApiErrors.LOGIN_PART_INVALID_PASSWORD)) {
            message = ApiErrors.LOGIN_ERROR_INVALID_USERNAME_PASSWORD;
        }

        if (message == null) {
            message = ApiErrors.LOGIN_ERROR_GENERIC;
            extra = error;
        }

        view.hideLoginProgress();
        view.showError(Result.createError(message, extra));

        state.reset();
    }

    @Override
    public void onForgotPasswordPressed() {
        view.openForgotPasswordScreen();
    }

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view.hideLoginProgress();
        view.hideError();
        view = null;
    }

    @Override
    public LoginContract.View getView() {
        return view;
    }
}