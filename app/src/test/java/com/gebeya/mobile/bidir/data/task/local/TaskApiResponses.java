package com.gebeya.mobile.bidir.data.task.local;

import com.gebeya.mobile.bidir.data.base.remote.ApiResponses;
import com.gebeya.mobile.bidir.data.base.remote.ResponseLoader;
import com.google.gson.JsonObject;

/**
 * Helper class for providing JSON task API responses back to the testing component
 */
public final class TaskApiResponses {

    public static Throwable getUpdateStatusInvalidTaskId() {
        return ApiResponses.createError("Cannot read property 'email' of null");
    }

    public static JsonObject getAllInvalidStatus() throws Exception {
        return ResponseLoader.openObject("task/task-status-invalid.json");
    }

    public static JsonObject getAllInvalidRefType() throws Exception {
        return ResponseLoader.openObject("task/task-ref-type-invalid.json");
    }

    public static JsonObject getAllInvalidTaskType() throws Exception {
        return ResponseLoader.openObject("task/task-type-invalid.json");
    }

    public static JsonObject getAllTasksSuccessful() throws Exception {
        return ResponseLoader.openObject("task/task-get-all-success.json");
    }

    public static JsonObject getAllInvalidTaskUserCommentNull() throws Exception {
        return ResponseLoader.openObject("task/task-get-all-comment-user-null-invalid.json");
    }
}
