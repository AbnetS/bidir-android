package com.gebeya.mobile.bidir.ui.form.questions.controller;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.complexquestion.ComplexQuestion;
import com.gebeya.mobile.bidir.data.complexquestion.remote.BaseComplexQuestionParser;
import com.gebeya.mobile.bidir.data.complexquestion.remote.ComplexQuestionParser;
import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.gebeya.mobile.bidir.impl.util.Utils;
import com.gebeya.mobile.bidir.ui.form.questions.QuestionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation for the {@link QuestionController} interface.
 */
public class BaseQuestionController implements QuestionController {

    private final List<ComplexQuestion> master;
    private final List<Prerequisite> prerequisites;

    public BaseQuestionController() {
        master = new ArrayList<>();
        prerequisites = new ArrayList<>();
    }

    @NonNull
    @Override
    public QuestionStatus getComplexQuestionStatus(@NonNull ComplexQuestion question) {
//        final String type = question.type;
//        final boolean answered = complexQuestionAnswered(question);
//        if (answered) {
//            return QuestionStatus.ANSWERED;
//        }
//        if (type.equals(ComplexQuestionParser.TYPE_LOCAL_GROUPED)) {
//            return question.required ? QuestionStatus.UNANSWERED_MANDATORY : QuestionStatus.UNANSWERED_OPTIONAL;
//        } else {
//            final boolean locked = complexQuestionLocked(question);
//            if (locked) {
//                return QuestionStatus.LOCKED;
//            }
//
//            return question.required ? QuestionStatus.UNANSWERED_MANDATORY : QuestionStatus.UNANSWERED_OPTIONAL;
//        }

        final boolean answered = complexQuestionAnswered(question);
        if (answered) {
            return QuestionStatus.ANSWERED;
        }

        final boolean locked = complexQuestionLocked(question);
        if (locked) {
            return QuestionStatus.LOCKED;
        }

        return question.required ? QuestionStatus.UNANSWERED_MANDATORY : QuestionStatus.UNANSWERED_OPTIONAL;
    }

    @Override
    public boolean complexQuestionAnswered(@NonNull ComplexQuestion question) {
        final String type = question.type;
        switch (type) {
            case ComplexQuestionParser.TYPE_LOCAL_YES_NO:
            case ComplexQuestionParser.TYPE_LOCAL_FILL_IN_BLANK:
            case ComplexQuestionParser.TYPE_LOCAL_MULTIPLE_CHOICE:
            case ComplexQuestionParser.TYPE_LOCAL_SINGLE_CHOICE:
                return Utils.hasData(question.values);
            case ComplexQuestionParser.TYPE_LOCAL_GROUPED: {
                final List<String> subQuestionIds = question.subQuestions;
                final boolean hasMandatorySubQuestions = getMandatoryQuestionCount(subQuestionIds) != 0;
                final int length = subQuestionIds.size();
                boolean allMandatoryAnswered = false;
                for (int i = 0; i < length; i++) {
                    final String subQuestionId = subQuestionIds.get(i);
                    final ComplexQuestion subQuestion = getComplexQuestion(subQuestionId, master);
                    final boolean answered = complexQuestionAnswered(subQuestion);
                    if (hasMandatorySubQuestions) {
                        if (subQuestion.required && !answered) return false;
                        else if (subQuestion.required && answered) {allMandatoryAnswered = true;}
                    } else {
                        if (answered) return true;
                    }
                }
                if (allMandatoryAnswered) return true;
                else return false;
            }
        }
        return false;
    }

    private int getMandatoryQuestionCount(@NonNull List<String> subQuestionIds) {
        final int length = subQuestionIds.size();
        int count = 0;
        for (int i = 0; i < length; i++) {
            final String id = subQuestionIds.get(i);
            final ComplexQuestion question = getComplexQuestion(id, master);
            count += question.required ? 1 : 0;
        }
        return count;
    }

    @Override
    public boolean complexQuestionLocked(@NonNull ComplexQuestion question) {
        if (question.hasPrerequisites) {
            final List<Prerequisite> list = getComplexQuestionPrerequisites(question._id, prerequisites);
            final boolean satisfied = prerequisitesSatisfied(list);
            return !satisfied;
        } else {
            return false;
        }
    }

    @Override
    public void setData(@NonNull List<ComplexQuestion> master, @NonNull List<Prerequisite> prerequisites) {
        this.master.clear();
        this.master.addAll(master);

        this.prerequisites.clear();
        this.prerequisites.addAll(prerequisites);
    }

    @Override
    public void clearData() {
        master.clear();
        prerequisites.clear();
    }

    @NonNull
    @Override
    public List<ComplexQuestion> getChildQuestions(@NonNull ComplexQuestion parentQuestion) {
        final List<ComplexQuestion> questions = new ArrayList<>();
        if (parentQuestion.type.equals(BaseComplexQuestionParser.TYPE_LOCAL_GROUPED)) {
            final List<String> ids = parentQuestion.subQuestions;
            final int length = ids.size();
            for (int i = 0; i < length; i++) {
                questions.add(getComplexQuestion(ids.get(i), master));
            }
        }

        return questions;
    }

    @Override
    public List<String> getDependants(@NonNull ComplexQuestion question) {
        final int length = prerequisites.size();
        final List<String> dependants = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            final Prerequisite prerequisite = prerequisites.get(i);
            if (prerequisite.questionId.equals(question._id)) {
                dependants.add(prerequisite.parentQuestionId);
            }
        }

        return dependants;
    }

    private boolean prerequisitesSatisfied(@NonNull List<Prerequisite> prerequisites) {
        final int length = prerequisites.size();
        for (int i = 0; i < length; i++) {
            final Prerequisite prerequisite = prerequisites.get(i);
            final ComplexQuestion question = getComplexQuestion(prerequisite.questionId, master);
            final boolean answeredCorrectly = complexQuestionAnsweredWith(question, prerequisite.answer);
            if (!answeredCorrectly) {
                return false;
            }
        }
        return true;
    }

    private boolean complexQuestionAnsweredWith(@NonNull ComplexQuestion question, @NonNull String answer) {
        final boolean answered = complexQuestionAnswered(question);
        return answered && question.values.contains(answer);        // TODO: This will not work with a grouped question
    }

    @NonNull
    @Override
    public ComplexQuestion getComplexQuestion(@NonNull String id) {
        final int length = master.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion question = master.get(i);
            if (question._id.equals(id)) {
                return question;
            }
        }
        throw new RuntimeException("Could not find ComplexQuestion with ID: " + id);
    }

    @NonNull
    private ComplexQuestion getComplexQuestion(@NonNull String id, @NonNull List<ComplexQuestion> master) {
        final int length = master.size();
        for (int i = 0; i < length; i++) {
            final ComplexQuestion question = master.get(i);
            if (question._id.equals(id)) {
                return question;
            }
        }
        throw new RuntimeException("Could not find ComplexQuestion with ID: " + id);
    }

    @NonNull
    private List<Prerequisite> getComplexQuestionPrerequisites(@NonNull String parentQuestionId, @NonNull List<Prerequisite> prerequisites) {
        final int length = prerequisites.size();
        final List<Prerequisite> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            final Prerequisite prerequisite = prerequisites.get(i);
            if (prerequisite.parentQuestionId.equals(parentQuestionId)) {
                list.add(prerequisite);
            }
        }
        return list;
    }
}
