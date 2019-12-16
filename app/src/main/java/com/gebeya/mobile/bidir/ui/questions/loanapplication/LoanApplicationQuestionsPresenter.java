package com.gebeya.mobile.bidir.ui.questions.loanapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.answer.Answer;
import com.gebeya.mobile.bidir.data.answer.local.AnswerLocalSource;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.base.remote.QAHelper;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplication;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplicationRepoSource;
import com.gebeya.mobile.bidir.data.loanapplication.LoanApplicationStatusHelper;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.question.Status;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskRepoSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.network.RequestState;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionItemView;

import java.util.List;

import javax.inject.Inject;

/**
 * Presenter implementation for the loan application.
 */
public class LoanApplicationQuestionsPresenter implements LoanApplicationQuestionsContract.Presenter {

    @Inject
    LoanApplicationRepoSource loanApplicationRepo;
    @Inject AnswerLocalSource answerLocal;
    private String loanApplicationId;
    private boolean editable;
    private Client client;

    @Inject
    ClientRepoSource clientRepo;
    private RequestState state;
    @Inject PermissionLocalSource permissionLocal;
    @Inject SchedulersProvider schedulers;
    @Inject
    TaskRepoSource taskRepo;
    private Task task;
    private String taskId;

    private LoanApplication application;
    private List<Answer> answers;

    private LoanApplicationQuestionsContract.View view;

    public LoanApplicationQuestionsPresenter(@NonNull String loanApplicationId,
                                             boolean editable,
                                             @NonNull RequestState state,
                                             @NonNull String taskId) {
        this.loanApplicationId = loanApplicationId;
        this.editable = editable;
        this.state = state;
        this.taskId = taskId;

        Tooth.inject(this, Scopes.SCOPE_REPOSITORIES);
    }

    @Override
    public void start() {
        view.hideRemark(true);

/*        if (taskId != null) {
            view.hideRemark(false);
            taskRepo.getByType(taskId)
                    .flatMapObservable(t -> {
                        task = t;

                        return Observable.just(task);
                    })
                    .subscribe();
        }*/

/*        loanApplicationRepo
                .getOneByClient(loanApplicationId)
                .flatMapObservable(application -> {
                    this.application = application;
                    return clientRepo.fetch(application.clientId);
                })
                .flatMap(client -> {
                    this.client = client;
                    return answerLocal.getByReferenceType(QAHelper.REF_TYPE_LOAN_APPLICATION, application._id);
                })
                .subscribe(answers -> {
                    if (!answers.isEmpty()) {
                        this.answers = answers;
                        view.populateIndicators(answers);
                        view.showAnswers();
                        view.setToolbarTitle(
                                String.format(Locale.getDefault(), "%s %s",
                                        client.firstName, client.lastName
                                )
                        );
                        onChoiceToggled(0);

                        if (taskId != null) {
                            view.showRemark(task.comment);
                        }
                    }
                });*/

        if (state.loading()) {
            view.showUpdatingScreeningProgress();
            return;
        }

        if (state.complete()) {
            onUpdateComplete();
            return;
        }

        if (state.error()) {
            onUpdateFailed(state.getError());
        }

        if (editable) {
            // toggle visibility of save button
            permissionLocal
                    .hasPermission(PermissionHelper.ENTITY_LOAN, PermissionHelper.OPERATION_UPDATE)
                    .subscribe(view::toggleSaveButton);

            // toggle visibility of submit button
            permissionLocal
                    .hasPermission(PermissionHelper.ENTITY_LOAN, PermissionHelper.OPERATION_UPDATE)
                    .subscribe(view::toggleSubmitButton);

            // toggle visibility of accept button
            permissionLocal
                    .hasPermission(PermissionHelper.ENTITY_LOAN, PermissionHelper.OPERATION_AUTHORIZE)
                    .subscribe(view::toggleAcceptButton);

            // toggle visibility of decline button
            permissionLocal
                    .hasPermission(PermissionHelper.ENTITY_LOAN, PermissionHelper.OPERATION_AUTHORIZE)
                    .subscribe(view::toggleDeclineButton);

            permissionLocal
                    .hasPermission(PermissionHelper.ENTITY_LOAN, PermissionHelper.OPERATION_AUTHORIZE)
                    .subscribe(hasPermission -> {
                        if (hasPermission) {
                            view.toggleSubmitButton(false);
                        }
                    });
        } else {
            view.toggleSaveButton(false);
            view.toggleSubmitButton(false);

            permissionLocal
                    .hasPermission(PermissionHelper.ENTITY_LOAN, PermissionHelper.OPERATION_AUTHORIZE)
                    .subscribe(view::toggleAcceptButton);
            permissionLocal
                    .hasPermission(PermissionHelper.ENTITY_LOAN, PermissionHelper.OPERATION_AUTHORIZE)
                    .subscribe(view::toggleDeclineButton);
        }

        view.toggleDisableShield(!editable);
    }

    @Override
    public void onSaveClicked() {
        if (state.loading()) return;

        state.setLoading();
        view.showUpdatingScreeningProgress();

/*        loanApplicationRepo.updateApiQuestions(application)
                .flatMap(done -> clientRepo.fetch(client._id))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                            state.setComplete();
                            onUpdateComplete();
                        },
                        throwable -> {
                            state.setError(throwable);
                            onLoadingFailed(throwable);
                        });*/

    }

    @Override
    public void onSubmitClicked() {
        updateStatus(LoanApplicationStatusHelper.SUBMITTED, null);
    }

    @Override
    public void onAcceptClicked() {
        if (state.loading()) return;

        int position = getMandatoryIncomplete();
        if (position != -1) {
            view.showMandatoryMissingError();
            onAnswerSelected(position);
            return;
        }

        state.setLoading();
        view.showUpdatingScreeningProgress();

/*        loanApplicationRepo
                .updateApiQuestions(application)
                .flatMap(done -> loanApplicationRepo.declineAcceptFinal(application, LoanApplicationStatusHelper.ACCEPTED))
                .flatMap(done -> clientRepo.fetch(client._id))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                            state.setComplete();
                            onUpdateComplete();
                        },
                        throwable -> {
                            state.setError(throwable);
                            onLoadingFailed(throwable);
                        });*/
    }

    @Override
    public void onDeclineClicked() {
        if (state.loading()) return;

        int position = getMandatoryIncomplete();
        if (position != -1) {
            view.showMandatoryMissingError();
            onAnswerSelected(position);
            return;
        }

        state.setLoading();
        view.showUpdatingScreeningProgress();

/*        loanApplicationRepo
                .updateApiQuestions(application)
                .flatMap(done -> loanApplicationRepo.declineAcceptFinal(application, LoanApplicationStatusHelper.DECLINED_FINAL))
                .flatMap(done -> clientRepo.fetch(client._id))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                            state.setComplete();
                            onUpdateComplete();
                        },
                        throwable -> {
                            state.setError(throwable);
                            onLoadingFailed(throwable);
                        });*/
    }

    @Override
    public void onDeclineReturned(@NonNull String remark, boolean isFinal) {
        final String status = isFinal ? LoanApplicationStatusHelper.DECLINED_FINAL : LoanApplicationStatusHelper.DECLINED_UNDER_REVIEW;
        updateStatus(status, remark);
    }

    private void updateStatus(@NonNull String status, @Nullable String remark) {
        if (state.loading()) return;

        int position = getMandatoryIncomplete();
        if (position != -1) {
            view.showMandatoryMissingError();
            onAnswerSelected(position);
            return;
        }

        state.setLoading();
        view.showUpdatingScreeningProgress();

/*        loanApplicationRepo
                .updateApiQuestions(application)
                .flatMap(done -> loanApplicationRepo.submitCropACAT(application, status, remark))
                .flatMap(done -> clientRepo.fetch(client._id))
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(done -> {
                            state.setComplete();
                            onUpdateComplete();
                        },
                        throwable -> {
                            state.setError(throwable);
                            onLoadingFailed(throwable);
                        });*/
    }

    @Override
    public int getMandatoryIncomplete() {
        final int size = answers.size();
        for (int i = 0; i < size; i++) {
            final Status status = getAnswerStatus(answers.get(i));
            if (status == Status.INCOMPLETE_MANDATORY) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getIncompleteQuestions() {
        final int size = answers.size();
        for (int i = 0; i < size; i++) {
            final Status status = getAnswerStatus(answers.get(i));
            if (status == Status.INCOMPLETE_MANDATORY || status == Status.INCOMPLETE_OPTIONAL) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void attachView(LoanApplicationQuestionsContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public LoanApplicationQuestionsContract.View getView() {
        return view;
    }

    @Override
    public void onBackButtonPressed() {
        view.close();
    }

    @Override
    public Status getAnswerStatus(Answer answer) {
        if (answer.selected) return Status.SELECTED;
        if (answer.answers.isEmpty()) {
            return answer.required ? Status.INCOMPLETE_MANDATORY : Status.INCOMPLETE_OPTIONAL;
        } else {
            return Status.COMPLETED;
        }
    }

    @Override
    public void onBindRowView(QuestionItemView view, int position) {
        final Answer answer = answers.get(position);

        view.setNumber(String.valueOf(answer.number));
/*        view.setChoices(answer.choices, answer.answers, answer.type);
        view.setComment(answer.remark);
        view.setToolbarTitle(answer.message);
        view.setSelected(answer.selected);
        view.setMandatory(answer.required);*/
    }

    @Override
    public void onAnswerSelected(int position) {
        final int size = answers.size();
        for (int i = 0; i < size; i++) {
            Answer answer = answers.get(i);
            answer.selected = position == i;
        }
        view.refreshAnswers();
        view.updateIndicators(answers);
        view.scrollToAnswer(position);
    }

    @Override
    public void onAnswerProvided(int position, List<String> answers) {
        final Answer answer = this.answers.get(position);
        answer.answers.clear();
        if (answer.type == QAHelper.TYPE_INPUT) {
            if (!answers.get(0).isEmpty()) {
                answer.answers.addAll(answers);
            }
        } else {
            answer.answers.addAll(answers);
            view.refreshAnswers();
        }
        view.updateIndicators(this.answers);
        answerLocal.put(answer);
    }

    @Override
    public int getAnswerCount() {
        return answers.size();
    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.showUpdateSuccessMessage();
        view.close();

        state.reset();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        String message = ApiErrors.SCREENING_QUESTIONS_UPDATE_GENERIC_ERROR;

        view.hideUpdatingScreeningProgress();
        view.showError(Result.createError(message, error));

        state.reset();
    }
}