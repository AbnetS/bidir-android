package com.gebeya.mobile.bidir.data.task;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.local.crud.ReadableSource;
import com.gebeya.mobile.bidir.data.base.local.crud.base.ReadSize;
import com.gebeya.mobile.bidir.data.base.repo.FetchMany;

import io.reactivex.Completable;

/**
 * Repository contract for the Task objects.
 */
public interface TaskRepoSource extends FetchMany<Task>, ReadableSource<Task>, ReadSize {

    /**
     * Update the given Task parameter on the remote API, with the new status along with the provided comment.
     *
     * @param task    Task to saveLoanProposal on the remote API
     * @param status  new status to saveLoanProposal of the task
     */
    Completable pushStatus(@NonNull Task task, @NonNull String status, @NonNull String comment);
}
