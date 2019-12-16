package com.gebeya.mobile.bidir.data.task.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.task.Task;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract interface for a remote source of Tasks.
 */
public interface TaskRemoteSource {

    /**
     * Download all the {@link Task} objects from the API.
     *
     * @return Observable list of all the Tasks.
     */
    Observable<List<Task>> getAll();

    /**
     * Update the given {@link Task} status along with the provided comment.
     *
     * @param task    Task to saveLoanProposal.
     * @param status  New status of the task.
     * @param comment Additional comment to be updated on the task.
     * @return Task that has been updated.
     */
    Observable<Task> updateStatus(@NonNull Task task, @NonNull String status, @NonNull String comment);

    /**
     * Convert the given JsonObject into a {@link Task} object.
     *
     * @throws Exception Thrown if there was a problem with the parsing.
     */
    Task parse(@NonNull JsonObject object) throws Exception;
}