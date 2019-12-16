package com.gebeya.mobile.bidir.data.task.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.base.remote.BaseRemoteSource;
import com.gebeya.mobile.bidir.data.base.remote.backend.Service;
import com.gebeya.mobile.bidir.data.screening.ScreeningStatusHelper;
import com.gebeya.mobile.bidir.data.task.Task;
import com.gebeya.mobile.bidir.data.task.TaskHelper;
import com.gebeya.mobile.bidir.toothpick.Scopes;
import com.gebeya.mobile.bidir.toothpick.Tooth;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Concrete implementation of the {@link TaskRemoteSource} interface.
 */
public class TaskRemote extends BaseRemoteSource<TaskService> implements TaskRemoteSource {

    @Inject TaskHelper taskHelper;
    @Inject ScreeningStatusHelper statusHelper;

    public TaskRemote() {
        Tooth.inject(this, Scopes.SCOPE_REMOTE_SOURCES);
        setParams(Service.TASKS, TaskService.class);
    }

    @Override
    public Observable<List<Task>> getAll() {
        return build().call(service.getAll())
                .map(object -> {
                    final JsonArray array = object.get("docs").getAsJsonArray();
                    final int size = array.size();
                    final List<Task> tasks = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        final JsonObject taskObject = array.get(i).getAsJsonObject();
                        final Task task = parse(taskObject);
                        if (task != null) {
                            tasks.add(task);
                        }
                    }
                    return tasks;
                });
    }

    @Override
    public Observable<Task> updateStatus(@NonNull Task task, @NonNull String status, @NonNull String comment) {
        final Map<String, String> request = new HashMap<>();

        request.put("comment", comment);
        request.put("status", statusHelper.getApiStatus(status));

        return build().call(service.updateStatus(task._id, request)).map(this::parse);
    }

    @Override
    public Task parse(@NonNull JsonObject object) throws Exception {
        try {
            final Task task = new Task();

            task._id = object.get("_id").getAsString();

            JsonElement taskElement = object.get("task");
            if (taskElement == null || taskElement.isJsonNull()) return null;
            task.description = taskElement.getAsString();

            final String updatedAt = object.get("last_modified").getAsString();
            task.updatedAt = new DateTime(updatedAt);

            final String apiType = object.get("task_type").getAsString();
            final String localType = taskHelper.getLocalType(apiType);
            if (localType.equals(TaskHelper.UNKNOWN_TYPE)) {
                throw new Exception("Unknown API Task type: " + apiType);
            }
            task.type = localType;

            task.referenceId = object.get("entity_ref").getAsString();

            final String apiRefType = object.get("entity_type").getAsString();
            final String localRefType = taskHelper.getLocalRef(apiRefType);
            if (localRefType.equals(TaskHelper.UNKNOWN_REF)) {
                throw new Exception("Unknown API Task Ref type: " + apiRefType);
            }
            task.referenceType = localRefType;

            task.createdBy = object.get("created_by").getAsString();

            final JsonElement commentElement = object.get("comment");
            final String comment;
            if (commentElement.isJsonNull()) {
                comment = "-";
            } else {
                comment = commentElement.getAsString();
            }
            task.comment = comment;

            final String apiStatus = object.get("status").getAsString();
            final String localStatus = taskHelper.getLocalStatus(apiStatus);
            if (localStatus.equals(TaskHelper.UNKNOWN_STATUS)) {
                throw new Exception("Unknown API Task status: " + apiStatus);
            }
            task.status = localStatus;

            final JsonElement userElement = object.get("user");
            final String userId;
            if (userElement.isJsonNull()) {
                userId = "-";
            } else {
                userId = userElement.getAsString();
            }
            task.userId = userId;

            return task;
        } catch (Exception e) {
            throw new Exception("Parsing Task failed: " + e.toString());
        }
    }
}