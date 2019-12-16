package com.gebeya.mobile.bidir.data.prerequisite.remote;

import android.support.annotation.NonNull;

import com.gebeya.mobile.bidir.data.prerequisite.Prerequisite;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Concrete implementation for the {@link PrerequisiteParser} interface.
 */
public class BasePrerequisiteParser implements PrerequisiteParser {

    @Override
    public Prerequisite parse(@NonNull JsonObject object, @NonNull String parentQuestionId) throws Exception {
        try {
            final Prerequisite prerequisite = new Prerequisite();

            prerequisite._id = object.get("_id").getAsString();
            prerequisite.questionId = object.get("question").getAsString();
            prerequisite.parentQuestionId = parentQuestionId;
            prerequisite.answer = object.get("answer").getAsString();

            return prerequisite;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error parsing Prerequisite: " + e.getMessage());
        }
    }

    @Override
    public JsonArray createPrerequisites(@NonNull List<Prerequisite> prerequisites) {
        final JsonArray array = new JsonArray();
        final int length = prerequisites.size();

        for (int i = 0; i < length; i++) {
            final Prerequisite prerequisite = prerequisites.get(i);
            final JsonObject object = createPrerequisite(prerequisite);
            array.add(object);
        }

        return array;
    }

    @Override
    public JsonObject createPrerequisite(@NonNull Prerequisite prerequisite) {
        final JsonObject object = new JsonObject();

        object.addProperty("_id", prerequisite._id);
        object.addProperty("question", prerequisite.questionId);
        object.addProperty("answer", prerequisite.answer);

        return object;
    }
}
