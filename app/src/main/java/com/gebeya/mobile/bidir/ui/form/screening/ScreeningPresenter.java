package com.gebeya.mobile.bidir.ui.form.screening;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.apps.framework.util.Result;
import com.gebeya.mobile.bidir.data.base.remote.ApiErrors;
import com.gebeya.mobile.bidir.data.client.Client;
import com.gebeya.mobile.bidir.data.client.ClientRepoSource;
import com.gebeya.mobile.bidir.data.client.GroupedClient;
import com.gebeya.mobile.bidir.data.complexloanapplication.repo.ComplexLoanApplicationRepositorySource;
import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.local.ComplexQuestionLocalSource;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.data.complexscreening.ComplexScreening;
import com.gebeya.mobile.bidir.data.complexscreening.repo.ComplexScreeningRepositorySource;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.GroupedComplexScreening;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.remote.GroupedComplexScreeningParser;
import com.gebeya.mobile.bidir.data.groupedcomplexscreening.repo.GroupedComplexScreeningRepoSource;
import com.gebeya.mobile.bidir.data.groups.GroupRepoSource;
import com.gebeya.mobile.bidir.data.permission.Permission;
import com.gebeya.mobile.bidir.data.permission.PermissionHelper;
import com.gebeya.mobile.bidir.data.permission.local.PermissionLocalSource;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.data.prerequisite.local.PrerequisiteLocalSource;
import com.gebeya.mobile.bidir.data.screening.ScreeningStatusHelper;
import com.gebeya.mobile.bidir.data.section.Section;
import com.gebeya.mobile.bidir.data.section.local.SectionComparator;
import com.gebeya.mobile.bidir.data.section.local.SectionLocalSource;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskHelper;
import com.gebeya.mobile.bidir.data.task.TaskRepoSource;
import com.gebeya.mobile.bidir.impl.rx.SchedulersProvider;
import com.gebeya.mobile.bidir.impl.util.Constants;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionGroupedViewHolder;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionItemView;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionNormalViewHolder;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionStatus;
import com.gebeya.mobile.bidir.ui.form.questions.controller.QuestionController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * Concrete {@link ScreeningContract.Presenter} presenter implementation.
 */
public class ScreeningPresenter implements ScreeningContract.Presenter {

    private ScreeningContract.View view;

    private final String screeningId;
    private final String groupId;
    private Client client;
    private Task task;
    private ComplexScreening screening;

    private int activeQuestionPosition;

    private final List<ComplexQuestion> master;
    private final List<ComplexQuestion> questions;
    private final List<Prerequisite> prerequisites;
    private final List<Section> sections;

    private final List<Permission> permissions;

    private boolean editable;

    @Inject PrerequisiteLocalSource prerequisiteLocal;
    @Inject ComplexScreeningRepositorySource screeningRepo;
    @Inject ComplexLoanApplicationRepositorySource loanApplicationRepo;
    @Inject TaskRepoSource taskRepo;
    @Inject ComplexQuestionLocalSource questionLocal;
    @Inject ClientRepoSource clientRepo;
    @Inject SectionLocalSource sectionLocal;
    @Inject PermissionLocalSource permissionLocal;

    @Inject QuestionController questionController;

    @Inject ScreeningLoadState loadState;
    @Inject ScreeningUpdateState updateState;

    @Inject SchedulersProvider schedulers;
    @Inject GroupRepoSource groupRepo;
    @Inject GroupedComplexScreeningRepoSource groupedScreeningRepo;

    public ScreeningPresenter(@NonNull String screeningId, @Nullable Task task, @Nullable String groupId) {
        this.screeningId = screeningId;
        this.task = task;
        this.groupId = groupId;

        if (task != null) {
            editable = task.type.equals(TaskHelper.TYPE_REVIEW);
        } else {
            editable = true;
        }

        master = new ArrayList<>();
        questions = new ArrayList<>();
        prerequisites = new ArrayList<>();
        sections = new ArrayList<>();
        permissions = new ArrayList<>();

        activeQuestionPosition = 0;

        Tooth.inject(this, Scopes.SCOPE_STATES);
    }

    @Override
    @SuppressLint("CheckResult")
    public void start() {
        // Update state restoration
        if (updateState.loading()) {
            view.showUpdateProgress();
            return;
        }
        if (updateState.complete()) {
            onUpdateComplete();
            return;
        }
        if (updateState.error()) {
            onUpdateFailed(updateState.getError());
            return;
        }

        // Loading state restoration
        if (loadState.loading()) {
            view.showLoadingProgress();
            return;
        }
        if (loadState.complete()) {
            onLoadingComplete();
            return;
        }
        if (loadState.error()) {
            onLoadingFailed(loadState.getError());
            return;
        }

        loadState.setLoading();

        screeningRepo.fetch(screeningId)
                .flatMap(complexScreening -> {
                    screening = complexScreening;
                    return questionLocal.getAll(screeningId, Constants.REF_TYPE_SCREENING);
                })
                .flatMap(complexQuestions -> {
                    master.clear();
                    master.addAll(complexQuestions);
                    return sectionLocal.getAll(screeningId, Constants.REF_TYPE_SCREENING);
                })
                .flatMap(list -> {
                    sections.clear();
                    sections.addAll(list);
                    buildQuestions();
                    return clientRepo.fetch(screening.clientId);
                })
                .flatMap(c -> {
                    client = c;
                    return prerequisiteLocal.getAll();
                })
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                            prerequisites.clear();
                            prerequisites.addAll(list);

                            loadState.setComplete();
                            onLoadingComplete();
                        },
                        throwable -> {
                            loadState.setError(throwable);
                            onLoadingFailed(throwable);
                        });

        permissionLocal.getByEntity(PermissionHelper.ENTITY_SCREENING)
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe(list -> {
                    if (view == null) return;

                    permissions.clear();
                    permissions.addAll(list);

                    applyPermissions();
                });
    }

    private void applyPermissions() {
        final boolean canUpdate = hasPermission(PermissionHelper.OPERATION_UPDATE);
        final boolean canAuthorize = hasPermission(PermissionHelper.OPERATION_AUTHORIZE);

        if (task == null) {     // Launched from regular Screening edit mode
            view.toggleSaveButton(canUpdate);
            if (canAuthorize) {
                view.toggleSubmitButton(false);
            } else {
                view.toggleSubmitButton(canUpdate);
            }
            view.toggleApproveButton(canAuthorize);
            view.toggleDeclineButton(canAuthorize);
        } else {                // Launched from a Task
            if (!task.type.equals(TaskHelper.TYPE_REVIEW)) {
                view.toggleSaveButton(false);
                view.toggleSubmitButton(false);
            } else {
                view.toggleSaveButton(canUpdate);
                view.toggleSubmitButton(canUpdate);
            }
            view.toggleApproveButton(canAuthorize);
            view.toggleDeclineButton(canAuthorize);
        }
    }

    private boolean hasPermission(@NonNull String operation) {
        final int length = permissions.size();
        for (int i = 0; i < length; i++) {
            final Permission permission = permissions.get(i);
            if (permission.operation.equals(operation)) {
                return true;
            }
        }
        return false;
    }

    private void buildQuestions() {
        questions.clear();
        final int length = master.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion question = master.get(i);
            if (!isSubQuestion(question)) {
                questions.add(question);
            }
        }
        if (screening.hasSections) {
            Collections.sort(sections, new SectionComparator());
            sortQuestions();
        }
    }

    private void sortQuestions() {
        final List<ComplexQuestion> sorted = new ArrayList<>();
        final int length = sections.size();
        for (int i = 0; i < length; i++) {
            final Section section = sections.get(i);
            final List<String> ids = section.questionIds;
            for (String id : ids) {
                final ComplexQuestion question = getQuestion(id);
                sorted.add(question);
            }
        }
        questions.clear();
        questions.addAll(sorted);
    }

    private ComplexQuestion getQuestion(@NonNull String id) {
        final int length = questions.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion question = questions.get(i);
            if (question._id.equals(id)) return question;
        }
        throw new RuntimeException("Could not find ComplexQuestion with ID: " + id);
    }

    private boolean isSubQuestion(@NonNull ComplexQuestion question) {
        final int length = master.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion q = master.get(i);
            if (q.subQuestions.contains(question._id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLoadingComplete() {
        if (view == null) return;

        view.setTitle(Utils.formatName(
                client.firstName,
                client.surname,
                client.lastName
        ));

        questionController.setData(master, prerequisites);

        view.hideLoadingProgress();

        if (task != null) {
            if (task.type.equals(TaskHelper.TYPE_REVIEW)) {
                view.toggleRemark(true);
                view.setRemark(task.comment);
            }
        } else {
            view.toggleRemark(false);
        }

        view.generateQuestionIndicators(questions);
        view.loadQuestions();
        view.toggleReadOnlyModeShield(!editable);

        loadState.reset();
    }

    @Override
    public void onLoadingFailed(@NonNull Throwable throwable) {
        throwable.printStackTrace();        // TODO: Finish this
    }

    @Override
    public void onUpdateComplete() {
        if (view == null) return;

        view.hideUpdateProgress();
        view.showUpdateSuccessfulMessage();

        updateState.reset();
        loadState.reset();

        view.close();
    }

    @Override
    public void onUpdateFailed(@NonNull Throwable throwable) {
        if (view == null) return;

        String error = throwable.getMessage();
        if (error == null) error = throwable.toString();

        if (error.equals(ApiErrors.COMMON_ERROR_NO_INTERNET_CONNECTION)) {
            onUpdateComplete();
            return;
        }

        final String message = ApiErrors.SCREENING_QUESTIONS_UPDATE_GENERIC_ERROR;

        view.hideUpdateProgress();
        view.showError(Result.createError(message, error));

        updateState.reset();
    }

    @Override
    public void onBackButtonPressed() {
        loadState.reset();
        updateState.reset();

        view.close();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onSaveClicked() {
        if (updateState.loading()) return;

        updateState.setLoading();
        view.showUpdateProgress();

        if (groupId == null) {
            screeningRepo.saveScreening(screeningId)
                    .andThen(clientRepo.fetchForce(client._id))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(ignore -> {
                        updateState.setComplete();
                        onUpdateComplete();
                    }, throwable -> {
                        updateState.setError(throwable);
                        onUpdateFailed(throwable);
                    });
        } else {
            screeningRepo.saveScreening(screeningId)
                    .andThen(groupedScreeningRepo.updateStatus(groupId))
                    .flatMap(done -> clientRepo.fetchForce(client._id))
                    .flatMap(done -> groupRepo.fetchForce(groupId))
                    .flatMap(done -> groupedScreeningRepo.fetchForce(groupId))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(ignore -> {
                        updateState.setComplete();
                        onUpdateComplete();
                    }, throwable -> {
                        updateState.setError(throwable);
                        onUpdateFailed(throwable);
                    });
        }
    }

    @Override
    @SuppressLint("CheckResult")
    public void onSubmitClicked() {
        if (updateState.loading()) return;

        final int position = getMandatoryPosition();
        if (position != -1) {
            view.showMandatoryMissingError();
            onQuestionSelected(position);
            return;
        }

        updateState.setLoading();
        view.showUpdateProgress();

        if (groupId == null) {
            screeningRepo.submitScreening(screeningId)
                    .andThen(clientRepo.fetchForce(client._id))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(done -> {
                        updateState.setComplete();
                        onUpdateComplete();
                    }, throwable -> {
                        updateState.setError(throwable);
                        onUpdateFailed(throwable);
                    });
        } else {
            screeningRepo.submitScreening(screeningId)
                    .andThen(groupedScreeningRepo.updateStatus(groupId))
                    .flatMap(done -> clientRepo.fetchForce(client._id))
                    .flatMap(done -> groupRepo.fetchForce(groupId))
                    .flatMap(done -> groupedScreeningRepo.fetchForce(groupId))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(done -> {
                        updateState.setComplete();
                        onUpdateComplete();
                    }, throwable -> {
                        updateState.setError(throwable);
                        onUpdateFailed(throwable);
                    });
        }
    }

    @Override
    @SuppressLint("CheckResult")
    public void onApproveClicked() {
        if (updateState.loading()) return;

        final int position = getMandatoryPosition();
        if (position != -1) {
            view.showMandatoryMissingError();
            onQuestionSelected(position);
            return;
        }

        updateState.setLoading();
        view.showUpdateProgress();

        if (groupId == null) {
            screeningRepo.approveScreening(screeningId)
                    .andThen(loanApplicationRepo.createLoanApplication(client))
                    .andThen(clientRepo.fetchForce(client._id))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(done -> {
                        updateState.setComplete();
                        onUpdateComplete();
                    }, throwable -> {
                        updateState.setError(throwable);
                        onUpdateFailed(throwable);
                    });
        } else {
            screeningRepo.approveScreening(screeningId)
                    .andThen(groupedScreeningRepo.updateStatus(groupId))
                    .flatMap(done -> clientRepo.fetchForce(client._id))
                    .flatMap(done -> groupRepo.fetchForce(groupId))
                    .flatMap(done -> groupedScreeningRepo.fetchForce(groupId))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(done -> {
                        updateState.setComplete();
                        onUpdateComplete();
                    }, throwable -> {
                        updateState.setError(throwable);
                        onUpdateFailed(throwable);
                    });
        }

    }

    @Override
    public void onDeclineClicked() {
        final int position = getMandatoryPosition();
        if (position != -1) {
            view.showMandatoryMissingError();
            onQuestionSelected(position);
            return;
        }

        view.openDeclineDialog();
    }

    @Override
    @SuppressLint("CheckResult")
    public void onDeclineReturned(@NonNull String remark, boolean isFinal) {
        if (updateState.loading()) return;

        updateState.setLoading();
        view.showUpdateProgress();

        final Completable completable;
        if (task == null) {
            completable = screeningRepo.declineScreening(screeningId, isFinal, remark);
        } else {
            completable = taskRepo.pushStatus(task,
                    isFinal ? ScreeningStatusHelper.API_DECLINED_FINAL : ScreeningStatusHelper.API_DECLINED_UNDER_REVIEW,
                    remark
            );
        }

        if (groupId == null) {
            completable
                    .andThen(clientRepo.fetchForce(client._id))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(ignore -> {
                                updateState.setComplete();
                                onUpdateComplete();
                            },
                            throwable -> {
                                updateState.setError(throwable);
                                onUpdateFailed(throwable);
                            });
        } else {
            completable
                    .andThen(groupedScreeningRepo.updateStatus(groupId))
                    .flatMap(done -> clientRepo.fetchForce(client._id))
                    .flatMap(done -> groupRepo.fetchForce(groupId))
                    .flatMap(done -> groupedScreeningRepo.fetchForce(groupId))
                    .subscribeOn(schedulers.background())
                    .observeOn(schedulers.ui())
                    .subscribe(done -> {
                        updateState.setComplete();
                        onUpdateComplete();
                    }, throwable -> {
                        updateState.setError(throwable);
                        onUpdateFailed(throwable);
                    });
        }

    }

    private int getMandatoryPosition() {
        final int length = questions.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion question = questions.get(i);
            final boolean required = question.required;
            final boolean answered = questionController.complexQuestionAnswered(question);
            if (required && !answered) {
                if (question.type.equals(ComplexQuestionParser.TYPE_LOCAL_GROUPED)) {
                    final boolean hasUnansweredMandatoryChildQuestions = hasUnansweredMandatoryChildQuestions(question);
                    return hasUnansweredMandatoryChildQuestions ? i : -1;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean hasUnansweredMandatoryChildQuestions(@NonNull ComplexQuestion question) {
        final List<String> ids = question.subQuestions;
        final int length = ids.size();
        for (int i = 0; i < length; i++) {
            final String id = ids.get(i);
            final ComplexQuestion childQuestion = questionController.getComplexQuestion(id);
            final boolean required = childQuestion.required;
            final boolean answered = questionController.complexQuestionAnswered(childQuestion);
            if (required && !answered) return true;
        }
        return false;
    }

    @Override
    public void onIndicatorClicked(int position) {
        activeQuestionPosition = position;
        view.refreshIndicators(questions);
        view.refreshQuestions();
        view.scrollToItem(position);
    }

    @Override
    public void onBindRowView(@NonNull QuestionItemView holder, int position) {
        final ComplexQuestion question = questions.get(position);
        holder.setNumber(String.valueOf(question.number));
        holder.setText(question.text);
        holder.setRequired(question.required);
        holder.setRemark(question.remark);
        holder.setSelected(position == activeQuestionPosition);
        if (holder instanceof QuestionNormalViewHolder) {
            ((QuestionNormalViewHolder) holder).loadValues(question);
        } else {
            final List<ComplexQuestion> childQuestions = questionController.getChildQuestions(question);
            ((QuestionGroupedViewHolder) holder).loadQuestions(
                    question,
                    position,
                    childQuestions
            );
        }

        if (screening.hasSections) {
            final Section section = getSectionAboveQuestion(question._id);

            if (section == null) {
                holder.toggleSection(false);
            } else {
                holder.toggleSection(true);
                holder.setSection(section.title);
            }
        } else {
            holder.toggleSection(false);
        }

        holder.setLocked(questionController.complexQuestionLocked(question));
    }

    @Nullable
    private Section getSectionAboveQuestion(@NonNull String questionId) {
        final int[] positions = getQuestionPositionInSections(questionId);
        if (positions == null) {
            return null;
        } else {
            return sections.get(positions[0]);
        }
    }

    @Nullable
    private int[] getQuestionPositionInSections(@NonNull String questionId) {
        final int length = sections.size();
        for (int i = 0; i < length; i++) {
            final Section section = sections.get(i);
            final List<String> ids = section.questionIds;
            final int count = ids.size();
            for (int j = 0; j < count; j++) {
                if (j == 0 && ids.get(j).equals(questionId)) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    @Override
    public void onQuestionSelected(int position) {
        activeQuestionPosition = position;
        view.refreshQuestions();
        view.refreshIndicators(questions);
        view.scrollToItem(position);
    }

    @Override
    public int getQuestionsCount() {
        return questions.size();
    }

    @NonNull
    @Override
    public String getQuestionType(int position) {
        final ComplexQuestion question = questions.get(position);
        return question.type;
    }

    @NonNull
    @Override
    public QuestionStatus getQuestionStatus(int position) {
        if (position == activeQuestionPosition) return QuestionStatus.SELECTED;

        final ComplexQuestion question = questions.get(position);

        return questionController.getComplexQuestionStatus(question);
    }

    @Override
    @SuppressLint("CheckResult")
    public void onInputAnswerProvided(@NonNull String answer, int position, @NonNull ComplexQuestion question) {
        if (!editable) return;

        final ComplexQuestion targetQuestion;
        final ComplexQuestion selectedQuestion = questions.get(position);

        if (selectedQuestion.type.equals(ComplexQuestionParser.TYPE_LOCAL_GROUPED)) {
            targetQuestion = question;
        } else {
            targetQuestion = selectedQuestion;
        }

        targetQuestion.values.clear();
        targetQuestion.values.add(answer);

        questionLocal.put(targetQuestion)
                .subscribeOn(schedulers.background())
                .subscribe();

        view.refreshIndicators(questions);
    }

    @Override
    public void onChoiceAnswerProvided(@NonNull List<String> answers, int parentPosition, @NonNull ComplexQuestion question) {
        if (!editable) return;

        final ComplexQuestion targetQuestion;
        final ComplexQuestion selectedQuestion = questions.get(parentPosition);

        if (selectedQuestion.type.equals(ComplexQuestionParser.TYPE_LOCAL_GROUPED)) {
            targetQuestion = question;
        } else {
            targetQuestion = selectedQuestion;
        }

        final String type = targetQuestion.type;
        final boolean singleChoice = type.equals(ComplexQuestionParser.TYPE_LOCAL_SINGLE_CHOICE) ||
                type.equals(ComplexQuestionParser.TYPE_LOCAL_YES_NO);

        targetQuestion.values.clear();
        targetQuestion.values.addAll(answers);

        if (singleChoice) {
            view.refreshQuestion(parentPosition);
        }

        final List<String> dependants = questionController.getDependants(targetQuestion);
        if (!dependants.isEmpty()) {
            view.refreshQuestions();
        }

        questionLocal.put(targetQuestion)
                .subscribeOn(schedulers.background())
                .subscribe();

        view.refreshIndicators(questions);
    }

    @Override
    public void attachView(ScreeningContract.View view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public ScreeningContract.View getView() {
        return view;
    }
}